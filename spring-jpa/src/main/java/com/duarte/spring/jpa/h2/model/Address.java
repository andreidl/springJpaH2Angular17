package com.duarte.spring.jpa.h2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.gson.annotations.SerializedName;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "addresses")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@SerializedName("logradouro")
	private String address;
	private String number;
	@SerializedName("complemento")
	private String complement;
	@SerializedName("cep")
	private String postalCode;
	@SerializedName("city")
	private String city;
	@SerializedName("estado")
	private String state;
	private String country;

	@ManyToOne
	@JoinColumn(name = "id_client", nullable = false)
	@JsonBackReference 
	private Client client;

	public Address() {

	};

	public Address(String address, String number, String complement, String postalCode, String city, String state,
			String country, Client client) {
		super();
		this.address = address;
		this.number = number;
		this.complement = complement;
		this.postalCode = postalCode;
		this.city = city;
		this.state = state;
		this.country = country;
		this.client = client;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", address=" + address + ", number=" + number + ", complement=" + complement
				+ ", postalCode=" + postalCode + ", city=" + city + ", state=" + state + ", country=" + country
				+ ", client=" + client + "]";
	}

}
