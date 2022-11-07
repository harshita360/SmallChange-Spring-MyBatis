package com.fidelity.utils;

public class AuthenticationData {
	
	private String email;
	private String password;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String pasword) {
		this.password = pasword;
	}
	public AuthenticationData(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	
	

}
