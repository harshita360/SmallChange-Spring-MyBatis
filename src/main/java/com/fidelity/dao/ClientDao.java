package com.fidelity.dao;

import java.math.BigInteger;

import com.fidelity.models.Client;

public abstract class ClientDao {
	
	public abstract Client registerNewUser(Client client) ;
	public abstract Client authenticateUser(String email,String password) ; //4
	public abstract void removeUserById(BigInteger clientId) ; //3
	public abstract Client getUserById(BigInteger clientId);//1
	public abstract Client getUserByEmail(String email) ;//2
}
