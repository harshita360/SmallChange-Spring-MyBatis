package com.fidelity.restservices;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fidelity.exceptions.ClientException;
import com.fidelity.exceptions.FmtsException;
import com.fidelity.security.JwtTokenService;
import com.fidelity.service.ClientService;
import com.fidelity.utils.AuthenticationData;
import com.fidelity.utils.TokenDto;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CrossOrigin("*")
@RestController
@RequestMapping("/clients")
public class ClientController {
	
	@Autowired
	private ClientService service;
	
	@Autowired
	JwtTokenService tokenService;
	
	@Autowired
	Logger logger;
	
	@CircuitBreaker(name = "fmts-circuit-breaker")
	@PostMapping("/login")
	public ResponseEntity<TokenDto> authenticateUser(
			@RequestBody AuthenticationData data){
		logger.debug("IN Client Login");
		try {
			TokenDto responseData=service.authenticateUser(data.getEmail(), data.getPassword());
			return ResponseEntity.ok(responseData);
		}catch(ClientException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user name or password",e);
		
		}catch(FmtsException e) {
			throw new FmtsException(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error",e);
		}
	}
	
	@GetMapping("/data")
	public Map<String,Object> getHeaderDivision(
			@RequestHeader(value="Authorization", required=true)String value  ){
		Map<String,Object> resp=new HashMap<>();
		resp.put("claims", tokenService.extractAllClaims(value));
		resp.put("clientId", tokenService.extractClientId(value));
		return resp;
	}

}
