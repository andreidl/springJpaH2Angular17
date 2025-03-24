package com.duarte.spring.jpa.h2.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.duarte.spring.jpa.h2.model.Address;
import com.duarte.spring.jpa.h2.model.Client;
import com.duarte.spring.jpa.h2.service.AddressService;
import com.duarte.spring.jpa.h2.service.ClientService;
import com.google.gson.Gson;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class ClientController {

	@Autowired
	ClientService clientService;

	@Autowired
	AddressService addressService;

	@GetMapping("/clients")
	public ResponseEntity<List<Client>> getAllClients(@RequestParam(required = false) String firstName) {
		try {
			Set<Client> clients = new HashSet<>();

			if (firstName == null) {
				clientService.findAll().forEach(client -> {
					Client updatedClient = addMostRecentAddressToClient(client);
					clients.add(updatedClient);
				});
			} else {
				clientService.findByFirstNameContainingIgnoreCase(firstName).forEach(client -> {
					Client updatedClient = addMostRecentAddressToClient(client);
					clients.add(updatedClient);
				});
			}

			Logger logger = LoggerFactory.getLogger(getClass());
			logger.info("Clients retrieved: " + clients.size());

			if (clients.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(new ArrayList<>(clients), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/clients/{id}")
	public ResponseEntity<Client> getClientById(@PathVariable("id") long id) {
		Optional<Client> clientData = clientService.findById(id);

		if (clientData.isPresent()) {
			Client client = clientData.get();
			Client updatedClient = addMostRecentAddressToClient(client);
			return new ResponseEntity<>(updatedClient, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private Client addMostRecentAddressToClient(Client client) {
		if (client.getAddresses() == null || client.getAddresses().isEmpty()) {
			return client;
		}

		Address mostRecentAddress = client.getAddresses().stream().max(Comparator.comparing(Address::getId))
				.orElse(null);

		if (mostRecentAddress != null) {
			client.setPostalCode(mostRecentAddress.getPostalCode());
			client.setNumber(mostRecentAddress.getNumber());
		}

		return client;
	}

	public Address getClientCEP(String cep) {
		try {

			System.out.println("lol");
			String webService = "http://viacep.com.br/ws/";
			String urlParaChamada = webService + cep + "/json";

			URL url = new URL(urlParaChamada);
			HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

			if (conexao.getResponseCode() != 200)
				throw new RuntimeException("HTTP error code : " + conexao.getResponseCode());

			BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
			String jsonEmString = ClientService.converteJsonEmString(resposta);
			System.out.println(resposta);
			Gson gson = new Gson();
			Address address = gson.fromJson(jsonEmString, Address.class);
			System.out.println(address.toString());

			return address;
		} catch (Exception e) {
			return null;
		}

	}

	@PostMapping("/clients")
	public ResponseEntity<Client> createClient(@RequestBody Client client) {
		try {

			Address addressResponse = getClientCEP(client.getPostalCode());
			addressResponse.setClient(client);
			addressResponse.setNumber(client.getNumber());
			String cleanedPostalCode = addressResponse.getPostalCode().replace("-", "");
			addressResponse.setPostalCode(cleanedPostalCode);

			List<Address> listAddresses = new ArrayList<>();
			listAddresses.add(addressResponse);
			client.setAddresses(listAddresses);

			Client newClient = clientService.save(client);
			Address newAddress = addressService.save(addressResponse);
			return new ResponseEntity<>(newClient, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/clients/{id}")
	public ResponseEntity<Client> updateClient(@PathVariable("id") long id, @RequestBody Client client) {
		Optional<Client> clientData = clientService.findById(id);

		if (clientData.isPresent()) {
			Client newClient = clientData.get();

			newClient.setFirstName(client.getFirstName());
			newClient.setLastName(client.getLastName());
			newClient.setEmail(client.getEmail());

			List<Address> existingAddresses = addressService.findAllByClientId(newClient.getId());
			if (existingAddresses == null) {
				existingAddresses = new ArrayList<>();
			}

			boolean addressUpdated = false;

			if (client.getPostalCode() != null && !client.getPostalCode().isEmpty()) {
				for (Address existingAddress : existingAddresses) {
					if (existingAddress.getPostalCode().equals(client.getPostalCode())) {
						if (!existingAddress.getNumber().equals(client.getNumber())) {
							existingAddress.setNumber(client.getNumber());
							existingAddress.setPostalCode(client.getPostalCode().replace("-", ""));
							addressService.save(existingAddress);
						}
						addressUpdated = true;
						break;
					}
				}

				if (!addressUpdated) {
					Address newAddress = getClientCEP(client.getPostalCode());
					newAddress.setClient(newClient);
					newAddress.setNumber(client.getNumber());
					addressService.save(newAddress);
				}
			}

			Client updatedClient = clientService.save(newClient);

			return new ResponseEntity<>(updatedClient, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	@DeleteMapping("/clients/{id}")
	public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id") long id) {
		try {
			clientService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}