package com.fidelity.mappers;

import java.math.BigInteger;

import com.fidelity.models.Client;

public interface ClientMapper {
	Client registerNewUser(Client client);
	Client authenticateUser(String email,String password);
	void removeUserById(BigInteger clientId);
	Client getUserById(BigInteger clientId);
	Client getUserByEmail(String email);
}
