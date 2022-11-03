package com.fidelity.restservices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fidelity.models.Order;
import com.fidelity.models.Trade;

@CrossOrigin("*")
@RestController
@RequestMapping("/fmts/trades/trade")
public class TradeController {
    
	@PostMapping("/")
	public ResponseEntity<Trade> executeTrade(@RequestBody Order order)
	{
		
		
		
		return null;
		
	}
	
}
