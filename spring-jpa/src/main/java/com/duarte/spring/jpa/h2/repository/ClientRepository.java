package com.duarte.spring.jpa.h2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duarte.spring.jpa.h2.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

	List<Client> findByFirstNameContainingIgnoreCase(String firstName);

	boolean existsByPostalCode(String postalCode);
}