package com.fidelity.utils;

import java.math.BigInteger;

public class TokenDto {
	
	private String token;
	private String clientName;
	private BigInteger clientId;
	public TokenDto() {
		super();
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public BigInteger getClientId() {
		return clientId;
	}
	public void setClientId(BigInteger clientId) {
		this.clientId = clientId;
	}
	
	

}
