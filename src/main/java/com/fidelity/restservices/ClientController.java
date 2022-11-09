package com.fidelity.restservices;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fidelity.exceptions.ClientException;
import com.fidelity.models.Client;
import com.fidelity.security.JwtTokenService;
import com.fidelity.service.ClientService;
import com.fidelity.utils.AuthenticationData;
import com.fidelity.utils.TokenDto;

@CrossOrigin("*")
@RestController
@RequestMapping("/clients")
public class ClientController {
	
	@Autowired
	private ClientService service;
	
	@Autowired
	JwtTokenService tokenService;
	
	@PostMapping("/login")
	public ResponseEntity<TokenDto> authenticateUser(
			@RequestBody AuthenticationData data){
		try {
			TokenDto responseData=service.authenticateUser(data.getEmail(), data.getPassword());
			return ResponseEntity.ok(responseData);
		}catch(ClientException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user name or password",e);
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
	
	@PostMapping(path="/register",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Client> registerNewUser(@RequestBody Client client){
		System.out.println("Hi Adi 1");
		Client response=service.registerNewUser(client);
		
		System.out.println("Hi adi 2");		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
		
	}
	@DeleteMapping("/delete")
	public ResponseEntity<HttpStatus> removeUserById(@PathVariable BigInteger clientId) {
		System.out.println("Hi");
		service.removeUserById(clientId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
