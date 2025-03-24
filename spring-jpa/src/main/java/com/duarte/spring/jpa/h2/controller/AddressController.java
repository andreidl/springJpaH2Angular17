package com.duarte.spring.jpa.h2.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
public class AddressController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private AddressService addressService;
	
	@GetMapping("/addresses")
	public ResponseEntity<List<Address>> getAllAddresses(@RequestParam(required = false) String address) {
		try {
			Set<Address> addresses = new HashSet<>();

			if (address == null)
				addressService.findAll().forEach(addresses::add);
			else
				addressService.findByAddress(address).forEach(addresses::add);

	        Logger logger = LoggerFactory.getLogger(getClass());
	        logger.info("Addresses retrieved: " + addresses.size());
			
			if (addresses.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(new ArrayList<>(addresses), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/addresses/{id}")
	public ResponseEntity<Address> getClientById(@PathVariable("id") long id) {
		Optional<Address> addressData = addressService.findById(id);

		if (addressData.isPresent()) {
			return new ResponseEntity<>(addressData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@PostMapping("/addresses/{clientId}/")
	public ResponseEntity<Address> createAddress(@PathVariable(value = "clientId") String clientId,
			@RequestBody Address addressRequest) {
		try {
			long idClient = Long.parseLong(clientId);

			Optional<Client> optionalClient = clientService.findById(idClient);
			if (optionalClient.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Client client = optionalClient.get();

			Address newAddress = new Address();
			newAddress.setClient(client);
			
			Address addressResponse = getClientCEP(addressRequest.getPostalCode());
			
			
			newAddress.setAddress(addressResponse.getAddress());
			newAddress.setNumber(addressRequest.getNumber());
			newAddress.setComplement(addressResponse.getComplement());
			String cleanedPostalCode = addressResponse.getPostalCode().replace("-", "");
			newAddress.setPostalCode(cleanedPostalCode);
			newAddress.setCity(addressResponse.getCity());
			newAddress.setState(addressResponse.getState());
			newAddress.setCountry(addressResponse.getCountry());

			Address savedAddress = addressService.save(newAddress);

			return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

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

	@PutMapping("/addresses/{id}")
	public ResponseEntity<Address> updateAddress(@PathVariable("id") long id, @RequestBody Address addressRequest) {
		try {
			Optional<Address> optionalAddress = addressService.findById(id);
			if (optionalAddress.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Address newAddress = optionalAddress.get();
			newAddress.setClient(addressRequest.getClient());
			newAddress.setAddress(addressRequest.getAddress());
			newAddress.setNumber(addressRequest.getNumber());
			newAddress.setComplement(addressRequest.getComplement());
			String cleanedPostalCode = addressRequest.getPostalCode().replace("-", "");
			newAddress.setPostalCode(cleanedPostalCode);
			newAddress.setCity(addressRequest.getCity());
			newAddress.setState(addressRequest.getState());
			newAddress.setCountry(addressRequest.getCountry());
			Address savedAddress = addressService.save(newAddress);

			return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/addresses/{id}")
	public ResponseEntity<HttpStatus> deleteAddress(@PathVariable("id") long id) {
		addressService.deleteById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
