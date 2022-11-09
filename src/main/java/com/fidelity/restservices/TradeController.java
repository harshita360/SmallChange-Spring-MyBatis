package com.fidelity.restservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fidelity.models.Order;
import com.fidelity.models.Trade;
import com.fidelity.service.TradeService;

@CrossOrigin("*")
@RestController
@RequestMapping("/trade")
public class TradeController {
	
	@Autowired
	private TradeService service;
    
	@PostMapping("")
	public ResponseEntity<Trade> executeTrade(@RequestBody Order order,@RequestHeader("Authorization")String token)
	{
		
		try
		{
			Trade t= service.executeOrder(order,token.substring(6));
			if(t==null)
			{
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(t);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error",e);
			
		}
		
	}
	
}
