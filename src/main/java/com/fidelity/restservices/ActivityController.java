package com.fidelity.restservices;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fidelity.exceptions.DatabaseException;
import com.fidelity.exceptions.NotFoundException;
import com.fidelity.models.Trade;
import com.fidelity.service.ActivityService;

@CrossOrigin("*")
@RestController
@RequestMapping("/activity")
public class ActivityController {
	
	@Autowired
	ActivityService activityService;
	
	@GetMapping("/client/{clientId}")
	public ResponseEntity<List<Trade>> getUserActivity(@PathVariable BigInteger clientId){
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
	public ResponseEntity<List<Trade>> getPortfolioActivity(@PathVariable UUID portfolioId){
		List<Trade> activity=null;
		try {
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
