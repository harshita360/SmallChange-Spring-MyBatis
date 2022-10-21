package com.fidelity.models;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


public class Client {
	private BigInteger clientId;
	private String name;
	private String email;
	private String phone;
	public Client() {
		this.clientIdentification = new ClientIdentification[1];
	}
	public Client(BigInteger clientId, String name, String email, String password, String postalCode, String country, String phone,
			LocalDate dateOfBirth, BigInteger token, ClientIdentification[] clientIdentification,
			String investmentRiskAppetite) {
		super();
		this.clientId = clientId;
		this.name = name;
		this.email = email;
		this.password = password;
		this.postalCode = postalCode;
		this.country = country;
		this.dateOfBirth = dateOfBirth;
		this.token = token;
		this.clientIdentification = clientIdentification;
		this.investmentRiskAppetite = investmentRiskAppetite;
		this.phone = phone;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	private String password;
	private String postalCode;
	private String country;
	private LocalDate dateOfBirth;
	private BigInteger token;
	private ClientIdentification[] clientIdentification;
	private String investmentRiskAppetite;
	
	
	public void setClientIdentification(ClientIdentification clientIdentification) {
		this.clientIdentification = new ClientIdentification[] {clientIdentification};
	}
	public BigInteger getClientId() {
		return clientId;
	}
	public void setClientId(BigInteger clientId) {
		this.clientId = clientId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public BigInteger getToken() {
		return token;
	}
	public void setToken(BigInteger token) {
		this.token = token;
	}
	public ClientIdentification[] getClientIdentification() {
		return clientIdentification;
	}
//	public void setClientIdentification(List<ClientIdentification> clientIdentification) {
//		clientIdentification.toArray(this.clientIdentification);
//		//this.clientIdentification = (ClientIdentification[]) clientIdentification.stream().toArray();
//	}
	public String getInvestmentRiskAppetite() {
		return investmentRiskAppetite;
	}
	public void setInvestmentRiskAppetite(String investmentRiskAppetite) {
		this.investmentRiskAppetite = investmentRiskAppetite;
	}
	public boolean CheckPassword(String password2) {
		// TODO Auto-generated method stub
		if(password2.equals(this.password)) {
			return true;
		}
		return false;
	}
}


