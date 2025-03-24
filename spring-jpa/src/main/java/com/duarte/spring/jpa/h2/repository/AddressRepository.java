package com.duarte.spring.jpa.h2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duarte.spring.jpa.h2.model.Address;
import com.duarte.spring.jpa.h2.model.Client;

public interface AddressRepository extends JpaRepository<Address, Long> {

	  List<Address> findByClient(Client client);
	  List<Address> findByClient_Id(Long clientId);
	  List<Address> findByAddress(String address);
	  List<Address> findAddressesByClientId(long clientId);
}