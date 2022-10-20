package com.fidelity.models;

import java.math.BigInteger;
import java.time.LocalDate;


public class Client {
	private BigInteger clientId;
	private String name;
	private String email;
	public Client(BigInteger clientId, String name, String email, String password, String postalCode, String country,
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
	}
	private String password;
	private String postalCode;
	private String country;
	private LocalDate dateOfBirth;
	private BigInteger token;
	private ClientIdentification clientIdentification[];
	private String investmentRiskAppetite;
	
	
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
	public void setClientIdentification(ClientIdentification[] clientIdentification) {
		this.clientIdentification = clientIdentification;
	}
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


