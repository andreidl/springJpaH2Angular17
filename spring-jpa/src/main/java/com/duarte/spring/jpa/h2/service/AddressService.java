package com.duarte.spring.jpa.h2.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duarte.spring.jpa.h2.model.Address;
import com.duarte.spring.jpa.h2.repository.AddressRepository;

@Service
public class AddressService {
	@Autowired
	private AddressRepository addressRepository;

	public List<Address> findAll() {
		return addressRepository.findAll();
	}

	public List<Address> findByAddress(String address) {
		return addressRepository.findByAddress(address);
	}

	public Address save(Address address) {
		return addressRepository.save(address);
	}

	public Optional<Address> findById(long id) {
		return addressRepository.findById(id);
	}

	public void deleteById(long id) {
		addressRepository.deleteById(id);

	}

	public List<Address> findAllByClientId(long clientId) {
		addressRepository.findAddressesByClientId(clientId);
		return null;
	}

}
