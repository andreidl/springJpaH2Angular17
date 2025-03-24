package com.duarte.spring.jpa.h2.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duarte.spring.jpa.h2.model.Client;
import com.duarte.spring.jpa.h2.repository.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	public Iterable<Client> findAll() {

		return clientRepository.findAll();
	}

	public Iterable<Client> findByFirstNameContainingIgnoreCase(String firstName) {

		return clientRepository.findByFirstNameContainingIgnoreCase(firstName);
	}

	public Optional<Client> findById(long id) {
		return clientRepository.findById(id);
	}

	public Client save(Client client) {
		return clientRepository.save(client);
	}

	public void deleteById(long id) {
		clientRepository.deleteById(id);

	}

	public boolean existsById(Long clientId) {
		return clientRepository.existsById(clientId);
	}

	public boolean existsByPostalCode(String postalCode) {
		return clientRepository.existsByPostalCode(postalCode);
	}

	public static String converteJsonEmString(BufferedReader bufferedReader) throws IOException {
		String resposta, jsonEmString = "";
		while ((resposta = bufferedReader.readLine()) != null) {
			jsonEmString += resposta;
		}
		return jsonEmString;
	}

}
