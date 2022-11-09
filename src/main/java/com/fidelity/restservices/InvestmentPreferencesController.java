package com.fidelity.restservices;

import java.math.BigInteger;

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

import com.fidelity.dao.InvestmentPreferenceDao;
import com.fidelity.models.InvestmentPreference;
import com.fidelity.security.JwtTokenService;
import com.fidelity.service.ClientService;
import com.fidelity.service.InvestmentPreferenceService;

@CrossOrigin("")
@RestController
@RequestMapping("/preference")
public class InvestmentPreferencesController {
	
	@Autowired
	private InvestmentPreferenceService service;
	
	@Autowired
	private Logger logger;
	
	@Autowired
	private JwtTokenService tokenService;
	

	@GetMapping("")
	public ResponseEntity<InvestmentPreference> getInvestmentPreference(@RequestHeader("Authorization")String token)
	{
		try
		{
			InvestmentPreference i=service.getInvestmentPref(token.substring(6));
			if(i==null)
			{
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(i);
		}
		catch(Exception e)
		{
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"backend err",e);
		}
	}
	
	@PostMapping("/updatepref")
	public ResponseEntity<InvestmentPreference> updateInvestmentPreference(
			@RequestBody InvestmentPreference i,@RequestHeader("Authorization")String token)
	{
		token=token.substring(6);
		BigInteger clientId=new BigInteger(tokenService.extractClientId(token));
		i.setClientId(clientId);
		
		System.out.println(i);
	   try
	   {
		   InvestmentPreference ip=service.updateInvestmentPref(i);
		   if(ip==null)
		   {
			   return ResponseEntity.noContent().build();
		   }
		   return ResponseEntity.ok(ip);
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
		  throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"backend err",e); 
	   }
	}
	
	@PostMapping("/addpref")
	public ResponseEntity<InvestmentPreference> addInvestmentPreference(@RequestBody InvestmentPreference i)
	{
	   try
	   {
		   InvestmentPreference ip=service.insertInvestmentPref(i);
		   if(ip==null)
		   {
			   return ResponseEntity.noContent().build();
		   }
		   return ResponseEntity.ok(ip);
	   }
	   catch(Exception e)
	   {
		  logger.error("Error in setting investment prference {}",e);
		  System.out.println(e.getMessage());
		  throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"backend err",e); 
	   }
	}
}
