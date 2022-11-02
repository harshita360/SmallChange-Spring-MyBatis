package com.fidelity.utils;

import java.io.Serializable;
import java.math.BigInteger;

public class FmtsClientModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigInteger clientId;
	private BigInteger token;
	private String email;
	public BigInteger getClientId() {
		return clientId;
	}
	public void setClientId(BigInteger clientId) {
		this.clientId = clientId;
	}
	public BigInteger getToken() {
		return token;
	}
	public void setToken(BigInteger token) {
		this.token = token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
	

}
