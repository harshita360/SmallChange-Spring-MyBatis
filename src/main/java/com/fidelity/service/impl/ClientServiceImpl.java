package com.fidelity.service.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.javassist.tools.web.BadHttpRequest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidelity.dao.ClientDao;
import com.fidelity.models.Client;
import com.fidelity.security.JwtTokenService;
import com.fidelity.service.ClientService;
import com.fidelity.utils.FmtsClientModel;
import com.fidelity.utils.TokenDto;

@Service
public class ClientServiceImpl extends ClientService{
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private ClientDao dao;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	@Autowired
	private JwtTokenService jwtService;

	@Override
	public Client registerNewUser(Client client) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public TokenDto authenticateUser(String email, String password) {
		
		Client client=dao.authenticateUser(email, password);
		
		FmtsClientModel respData=this.registerWithFmtsServer(client);
		
		logger.debug("In the response uccesful");
		TokenDto token=new TokenDto();
		token.setClientName(client.getName());
		Map<String,Object> claims=new HashMap<>(); 
		claims.put("ROLE", "CLIENT");
		claims.put("fmtsToken", respData.getToken());
		token.setToken(jwtService.createToken(claims, client.getClientId()));
		token.setClientId(respData.getClientId());
		return token;
		
	}

	@Override
	public Client getLoggedInUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserLoggedIn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void logoutUser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUserById(BigInteger clientId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Client getUserById(BigInteger clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Client getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private FmtsClientModel registerWithFmtsServer(Client client) {
		
		HttpHeaders headers=new HttpHeaders();
		headers.add("Cotent-Type", "application/json");
		headers.add("Accept", "application/json");
		
		FmtsClientModel requestData=new FmtsClientModel();
		requestData.setClientId(client.getClientId());
		requestData.setEmail(client.getEmail());
		
		
		HttpEntity<FmtsClientModel> requestEntity=new HttpEntity<>(requestData,headers);
		
		
		logger.debug("Going to issue request");
		ResponseEntity<String> response= restTemplate.postForEntity("http://localhost:3000/fmts/client/", requestEntity ,String.class);
		logger.debug(response.toString());

		if(response.getStatusCode().is2xxSuccessful() && response.getBody()!=null) {
			
			FmtsClientModel respData;
			try {
				respData = objectMapper.readValue(response.getBody(), FmtsClientModel.class);
			} catch (JsonProcessingException e) {
				logger.error("Json Decoder error from fmts server",e);
				throw new RuntimeException("JSON Error");
			}
			
			return respData;
		}else {
			throw new RuntimeException("Invalid authentication data");
		}
	}

}
