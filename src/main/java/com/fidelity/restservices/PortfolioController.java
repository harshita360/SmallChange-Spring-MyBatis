package com.fidelity.restservices;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fidelity.exceptions.DatabaseException;
import com.fidelity.exceptions.NotEligibleException;
import com.fidelity.exceptions.NotFoundException;
import com.fidelity.models.Portfolio;
import com.fidelity.service.PortfolioService;
import com.fidelity.utils.PortfolioUtils;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {
	
	@Qualifier("proxyPortfolioService")
	@Autowired
	private PortfolioService service;
	
	@GetMapping("/client/{clientId}")
	public ResponseEntity<List<Portfolio>> getAllClientsPortfolio(
			@PathVariable BigInteger clientId
			){
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
	
	@PostMapping("/client/{clientId}/default")
	public ResponseEntity<Portfolio> createClientDefaultPortfolio(
			@PathVariable BigInteger clientId
			){
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
	
	@PostMapping("/client/{clientId}")
	public ResponseEntity<Portfolio> createClientPortfolio(
			@PathVariable BigInteger clientId,
			@RequestBody Portfolio portfolio
			){
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
			@PathVariable UUID portfolioId
			){
		try {
			Portfolio portfolio=service.getPortfolioForAuserFromPortfolioId(portfolioId.toString());
			
			return ResponseEntity.ok(portfolio);
		}catch(NotFoundException e) {
			return ResponseEntity.notFound().build();
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server error",e);
		}
	}
	
	
	@DeleteMapping("/{portfolioId}")
	public ResponseEntity<Portfolio> deletePortfolioFromId(
			@PathVariable UUID portfolioId
			){
		try {
			service.deletePortfolioById(portfolioId.toString());
			
			return ResponseEntity.status(HttpStatus.OK).build();
		}catch(DatabaseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(),e);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Server error",e);
		}
	}

}
