package com.fidelity.service;

import java.math.BigInteger;

import com.fidelity.models.Client;
import com.fidelity.utils.TokenDto;

public abstract class ClientService {
	public abstract Client registerNewUser(Client client) ;
	public abstract TokenDto authenticateUser(String email,String password) ; //4
	public abstract Client getLoggedInUser();
	public abstract boolean isUserLoggedIn();
	public abstract void logoutUser();
	public abstract void removeUserById(BigInteger clientId) ; //3
	public abstract Client getUserById(BigInteger clientId);//1
	public abstract Client getUserByEmail(String email) ;//2
}

