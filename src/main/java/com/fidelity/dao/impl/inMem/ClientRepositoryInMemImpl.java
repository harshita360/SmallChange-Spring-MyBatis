package com.fidelity.dao.impl.inMem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fidelity.dao.ClientDao;
import com.fidelity.enums.ResourceType;
import com.fidelity.exceptions.ClientException;
import com.fidelity.models.Client;

@Component("clientDaoInMem")
public class ClientRepositoryInMemImpl extends ClientDao {

	private List<Client> clients = new ArrayList<>();
	private Client loggedInUser = null; 
	private static ClientRepositoryInMemImpl instance;
	
	public static ClientRepositoryInMemImpl getInstance(ResourceType resource) {
		if(resource.equals(ResourceType.PROTY_TYPE)) {
			return new ClientRepositoryInMemImpl();
		}
		if(instance==null) {
			synchronized (ClientRepositoryInMemImpl.class) {
				if(instance==null) {
					instance=new ClientRepositoryInMemImpl();
					System.out.println("created new repo");
				}
				
			}
		}
		return instance;
	}
	
	@Override
	public Client registerNewUser(Client client)  {
		if(this.getUserByEmail(client.getEmail())!=null) {
			throw new ClientException("Already user exist with this email");
		}
		this.clients.add(client);
		return client;
	}

	@Override
	public Client authenticateUser(String email, String password)  {
		Client client=this.getUserByEmail(email);
		if(client.getPassword()==password) {
			this.loggedInUser=client;
			return client;
		}
		throw new ClientException("Invalid email or password!!!");
	}

	public Client getLoggedInUser() {
		return this.loggedInUser;
	}

	public boolean isUserLoggedIn() {
		if(this.loggedInUser!=null) {
			return true;
		}
		return false;
	}

	@Override
	public void removeUserById(BigInteger clientId) {
		Client client = this.getUserById(clientId);
		this.clients.remove(client);
		
	}

	@Override
	public Client getUserById(BigInteger clientId)  {
		Client client = null;
		List<Client> filtered=this.clients.stream().filter( c-> c.getClientId().equals(clientId)).toList();
		if(filtered.size()==1) {
			client=filtered.get(0);
		}
		if(client==null) {
			throw new ClientException("User with requested client id not found");
		}
		return client;
	}

	@Override
	public Client getUserByEmail(String email)  {
		List<Client> value = clients.stream().filter(iter -> iter.getEmail().equals(email)).toList();
		if(value.size()==0) {
			return null;
		}
		return(value.get(0));
	}

	public void logoutUser() {
		this.loggedInUser=null;
	}
}
