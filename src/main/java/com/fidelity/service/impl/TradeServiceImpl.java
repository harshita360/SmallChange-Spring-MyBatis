package com.fidelity.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidelity.dao.ClientDao;
import com.fidelity.dao.PortfolioDao;
import com.fidelity.exceptions.IneligibleOrderException;
import com.fidelity.exceptions.InsufficientBalanceException;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.security.JwtTokenService;
import com.fidelity.models.Client;
import com.fidelity.service.ActivityService;
import com.fidelity.service.ClientService;
import com.fidelity.service.PortfolioService;
import com.fidelity.service.TradeService;
import com.fidelity.utils.FmtsClientModel;
import com.fidelity.utils.OrderDto;


@Service
public class TradeServiceImpl extends TradeService{
	
	@Autowired
	@Qualifier("proxyPortfolioService")
	private PortfolioService portfolioService;
	
	@Autowired
	private ClientDao clientdao;
	
	
	@Autowired
	private JwtTokenService jwtService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Autowired
	@Qualifier("proxyActivityService")
	private ActivityService activityService;
	

	


	@Override
	public Trade executeOrder(Order order,String token) throws Exception {
		// TODO Auto-generated method stub
		Trade t=null;
		Order o=activityService.addOrder(order);
		OrderDto orderdto=new OrderDto();
		orderdto.setClientId(order.getClientId());
		orderdto.setDirection(order.getDirection());
		orderdto.setInstrumentId(order.getInstrumentId());
		orderdto.setOrderId(order.getOrderId());
		orderdto.setPortfolioId(order.getPortfolioId());
		orderdto.setTargetPrice(order.getTargetPrice());
		orderdto.setQuantity(order.getQuantity());
		Client c=clientdao.getUserById(order.getClientId());
		orderdto.setEmail(c.getEmail());
		orderdto.setToken(new BigInteger(jwtService.getClaimsFmtsToken(token)));
		
		if(order.getDirection().equals("B"))
		{
			t=carryBuyTransaction(orderdto);
		}
		else if(order.getDirection().equals("S")){
			t=carrySellTransaction(orderdto);
		}
		else
		{
			throw new IneligibleOrderException("Not valid order");
		}
		//call activity service to add to trade activity table;
		t.getOrder().setOrderId(o.getOrderId());
		t.setPortfolioId(order.getPortfolioId());
		t.setTransactionAt(LocalDateTime.now());
		t.getOrder().setPortfolioId(order.getPortfolioId());
//		try
//		{
		activityService.addActivity(t);
		portfolioService.updatePortfolioFromTrade(t);
//		}
//		catch(Exception e)
//		{
//			
//			throw new Exception(e);
//		}
		return t;
		
	}

	
	private Trade carryBuyTransaction(OrderDto orderdto) throws Exception {
		// TODO Auto-generated method stub
		Trade t;
		Portfolio portfolio=portfolioService.getPortfolioForAuserFromPortfolioId(orderdto.getPortfolioId());
		//BigDecimal executionPrice=orderdto.getOrder().getTargetPrice().multiply(BigDecimal.valueOf(orderdto.getOrder().getQuantity()));
		//BigDecimal cashValue=executionPrice.add(BigDecimal.valueOf(3));
		if(!portfolio.checkBuyEligibility(orderdto.getTargetPrice(),orderdto.getQuantity())) {
			throw new IneligibleOrderException("Portfolio not allowed to do sumit order");
		}
		
		HttpHeaders headers=new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("Accept", "application/json");
		HttpEntity<OrderDto> requestEntity=new HttpEntity<>(orderdto,headers);
		ResponseEntity<String> response=restTemplate.postForEntity("http://localhost:3000/fmts/trades/trade",requestEntity,String.class);
		
        if(response.getStatusCode().is2xxSuccessful() && response.getBody()!=null) {
			
			try
			{
				t=objectMapper.readValue(response.getBody(),Trade.class);
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
				
				throw new RuntimeException("JSON Error");
			
			}
			
			return t;
		}else {
			throw new RuntimeException("Invalid authentication data");
		}
		
//		Trade trade=new Trade(UUID.randomUUID().toString(),orderdto.getOrder().getDirection(),orderdto.getOrder(),orderdto.getOrder().getClientId(),
//				orderdto.getOrder().getPortfolioId(),orderdto.getOrder().getInstrumentId(),LocalDateTime.now(),orderdto.getOrder().getQuantity(),executionPrice,cashValue);
//		
		
		
	
		
	}

	
	private Trade carrySellTransaction(OrderDto orderdto) throws Exception {
		// TODO Auto-generated method stub
		Trade t;
		Portfolio portfolio=portfolioService.getPortfolioForAuserFromPortfolioId(orderdto.getPortfolioId());
		
		BigDecimal executionPrice=orderdto.getTargetPrice().multiply(BigDecimal.valueOf(orderdto.getQuantity()));
		BigDecimal cashValue=executionPrice.add(BigDecimal.valueOf(3));
		
		if(!portfolio.checkSellEligibility(orderdto.getInstrumentId(),orderdto.getQuantity())) {
			throw new IneligibleOrderException("Portfolio not allowed to do sumit order");
		}
//		Trade trade=new Trade(UUID.randomUUID().toString(),order.getDirection(),order,order.getClientId(),
//				order.getPortfolioId(),order.getInstrumentId(),LocalDateTime.now(),order.getQuantity(),executionPrice,cashValue);	
//		
		HttpHeaders headers=new HttpHeaders();
		headers.add("Content-Type","application/json");
		headers.add("Accept", "application/json");
		HttpEntity<OrderDto> requestEntity=new HttpEntity<>(orderdto,headers);
		ResponseEntity<String> response=restTemplate.postForEntity("http://localhost:3000/fmts/trades/trade",requestEntity,String.class);
		
        if(response.getStatusCode().is2xxSuccessful() && response.getBody()!=null) {
        	try
			{
				t=objectMapper.readValue(response.getBody(),Trade.class);
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
				
				throw new RuntimeException("JSON Error");
			
			}	
			return t;
		}else {
			throw new RuntimeException("Invalid authentication data");
		}
		
	}

}
