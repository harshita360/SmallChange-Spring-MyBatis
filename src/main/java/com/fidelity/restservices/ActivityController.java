package com.fidelity.restservices;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fidelity.exceptions.DatabaseException;
import com.fidelity.exceptions.NotFoundException;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.security.JwtTokenService;
import com.fidelity.service.ActivityService;
import com.fidelity.service.PortfolioService;

@CrossOrigin("*")
@RestController
@RequestMapping("/activity")
public class ActivityController {
	
	@Qualifier("proxyActivityService")
	@Autowired
	ActivityService activityService;
	
	@Qualifier("proxyPortfolioService")
	@Autowired
	private PortfolioService portfolioService;
	
	@Autowired
	private JwtTokenService tokenService;
	
	@GetMapping("/client")
	public ResponseEntity<List<Trade>> getUserActivity(
			@RequestHeader("Authorization") String token){
		token=token.substring(6);
		BigInteger clientId=new BigInteger(tokenService.extractClientId(token));
		List<Trade> activity=null;
		try {
			activity=activityService.getUserActivity(clientId);
		}
		catch(NotFoundException e) {
			return ResponseEntity.notFound().build();
		
		}catch(DatabaseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(),e);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server error",e);
		}
		if(activity==null || activity.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(activity);
	}
	
	@GetMapping("/{portfolioId}")
	public ResponseEntity<List<Trade>> getPortfolioActivity(@PathVariable UUID portfolioId,
			@RequestHeader("Authorization") String token){
		token=token.substring(6);
		BigInteger clientId=new BigInteger(tokenService.extractClientId(token));
		List<Trade> activity=null;
		try {
			Portfolio portfolio=portfolioService.getPortfolioForAuserFromPortfolioId(portfolioId.toString());
			if(!portfolio.getClientId().equals(clientId)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			activity=activityService.getPortfolioActivity(portfolioId.toString());
		}
		catch(NotFoundException e) {
			return ResponseEntity.notFound().build();
		
		}catch(DatabaseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(),e);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server error",e);
		}
		if(activity==null || activity.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(activity);
	}
	

}
