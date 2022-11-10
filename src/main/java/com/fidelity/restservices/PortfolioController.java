package com.fidelity.restservices;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.fidelity.exceptions.DatabaseException;
import com.fidelity.exceptions.NotEligibleException;
import com.fidelity.exceptions.NotFoundException;
import com.fidelity.models.Portfolio;
import com.fidelity.security.JwtTokenService;
import com.fidelity.service.PortfolioService;
import com.fidelity.utils.PortfolioUtils;

@CrossOrigin("*")
@RestController
@RequestMapping("/portfolios")
public class PortfolioController {
	
	@Qualifier("proxyPortfolioService")
	@Autowired
	private PortfolioService service;
	
	@Autowired
	private JwtTokenService tokenService;
	
	@Autowired
	Logger logger;
	
	@GetMapping("/client")
	public ResponseEntity<List<Portfolio>> getAllClientsPortfolio(
			@RequestHeader("Authorization") String token
			){
		token=token.substring(6);
		BigInteger clientId=new BigInteger(tokenService.extractClientId(token));
		try {
			List<Portfolio> portfolios=service.getPortfoliosForAUser(clientId);
			if(portfolios.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(portfolios);
		}catch(NotFoundException e) {
			return ResponseEntity.notFound().build();
		
		}catch(DatabaseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(),e);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server error",e);
		}
	}
	
	@PostMapping("/client/default")
	public ResponseEntity<Portfolio> createClientDefaultPortfolio(
			@RequestHeader("Authorization") String token){
		logger.debug("In default create portfolio of client");
		token=token.substring(6);
		BigInteger clientId=new BigInteger(tokenService.extractClientId(token));
		
		try {
			Portfolio portfolioInserted=service.addNewPortfolio(PortfolioUtils.getDefaultPortfolio(clientId));
			return ResponseEntity.status(HttpStatus.CREATED).body(portfolioInserted);
		}catch(NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client not found",e);
		
		}catch(DatabaseException   e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(),e);
		}
		catch(NotEligibleException  e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(),e);
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server error",e);
			
		}
	}
	
	@PostMapping("/client")
	public ResponseEntity<Portfolio> createClientPortfolio(
			@RequestBody Portfolio portfolio,
			@RequestHeader("Authorization") String token){
		
		token=token.substring(6);
		BigInteger clientId=new BigInteger(tokenService.extractClientId(token));
		
		try {
			portfolio.setClientId(clientId);
			Portfolio portfolioInserted=service.addNewPortfolio(portfolio);
			return ResponseEntity.status(HttpStatus.CREATED).body(portfolioInserted);
		}catch(NotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "client not found",e);
		}
		catch(NotEligibleException  e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(),e);
		}
		catch(DatabaseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(),e);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server error",e);
		}
	}
	
	@GetMapping("/{portfolioId}")
	public ResponseEntity<Portfolio> getPortfolioFromItId(
			@PathVariable UUID portfolioId,
			@RequestHeader("Authorization") String token){
		
		token=token.substring(6);
		BigInteger clientId=new BigInteger(tokenService.extractClientId(token));
		
		try {
			Portfolio portfolio=service.getPortfolioForAuserFromPortfolioId(portfolioId.toString());
			if(!portfolio.getClientId().equals(clientId)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			return ResponseEntity.ok(portfolio);
		}catch(NotFoundException e) {
			return ResponseEntity.notFound().build();
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server error",e);
		}
	}
	
	
	@DeleteMapping("/{portfolioId}")
	public ResponseEntity<Portfolio> deletePortfolioFromId(
			@PathVariable UUID portfolioId,
			@RequestHeader("Authorization") String token){
		
		token=token.substring(6);
		BigInteger clientId=new BigInteger(tokenService.extractClientId(token));
		
		try {
			Portfolio portfolio=service.getPortfolioForAuserFromPortfolioId(portfolioId.toString());
			if(!portfolio.getClientId().equals(clientId)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			service.deletePortfolioById(portfolioId.toString());
			
			return ResponseEntity.status(HttpStatus.OK).build();
		}catch(DatabaseException e) {
			logger.error("Database exception in deletion of portfolio by id {}",e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(),e);
		}catch(NotFoundException e) {
			return ResponseEntity.notFound().build();
		}catch(Exception e) {
			logger.error("Server exception in deletion of portfolio by id {}",e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server error",e);
		}
	}

}
