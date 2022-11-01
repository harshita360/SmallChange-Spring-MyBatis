package com.fidelity.dao.impl.myBatis;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.dao.ClientDao;
import com.fidelity.exceptions.ClientException;
import com.fidelity.mappers.ClientMapper;
import com.fidelity.models.Client;

@Component
@Profile("my-batis")
public class ClientDaoMyBatisImpl extends ClientDao{
	
	private final Logger logger = LoggerFactory.getLogger(ClientDao.class);
	
	@Autowired
	ClientMapper mapper;

	@Override
	@Transactional
	public Client registerNewUser(Client client) {
		logger.debug("Register New User/ Insert");
		if(this.getUserByEmail(client.getEmail())!=null) {
			throw new ClientException("Already user exist with this email");
		}
		if(mapper.registerNewUser(client)==1){
			return client;
		}
		return null;
	}

	@Override
	@Transactional
	public Client authenticateUser(String email, String password) {
		// TODO Auto-generated method stub
		Client client = mapper.getUserByEmail(email);
		if(client.getPassword().equals(password)) {
			return client;
		}
		throw new ClientException("Invalid email or password!!!");
	}

	@Override
	@Transactional
	public void removeUserById(BigInteger clientId) {
		// TODO Auto-generated method stub
		logger.debug("Remove User By Id");
		mapper.removeUserById(clientId);
	}

	@Override
	@Transactional
	public Client getUserById(BigInteger clientId) {
		// TODO Auto-generated method stub
		logger.debug("Get User By Id");
		return mapper.getUserById(clientId);
	}

	@Override
	@Transactional
	public Client getUserByEmail(String email) {
		// TODO Auto-generated method stub
		logger.debug("Get User By Email");
		return mapper.getUserByEmail(email);
	}
}
