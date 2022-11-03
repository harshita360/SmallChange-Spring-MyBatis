package com.fidelity.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.fidelity.dao.ClientDao;
import com.fidelity.dao.PortfolioDao;
import com.fidelity.exceptions.IneligibleOrderException;
import com.fidelity.exceptions.InsufficientBalanceException;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.service.PortfolioService;
import com.fidelity.services.TradeService;

public class TradeServiceImpl extends TradeService{
	
	@Autowired
	private PortfolioService portfolioService;
	
	


	@Override
	public Trade executeOrder(Order order) throws Exception {
		// TODO Auto-generated method stub
		Trade t=null;
		if(order.getDirection().equals("B"))
		{
			t=carryBuyTransaction(order);
		}
		else if(order.getDirection().equals("S")){
			t=carrySellTransaction(order);
		}
		else
		{
			throw new IneligibleOrderException("Not valid order");
		}
		//call activity service to add to trade activity table;
		return t;
		
	}

	
	private Trade carryBuyTransaction(Order order) throws Exception {
		// TODO Auto-generated method stub
		
		Portfolio portfolio=portfolioService.getPortfolioForAuserFromPortfolioId(order.getPortfolioId());
		BigDecimal executionPrice=order.getTargetPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
		BigDecimal cashValue=executionPrice.add(BigDecimal.valueOf(3));
		if(!portfolio.checkBuyEligibility(order)) {
			throw new IneligibleOrderException("Portfolio not allowed to do sumit order");
		}
		
		Trade trade=new Trade(UUID.randomUUID().toString(),order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(),LocalDateTime.now(),order.getQuantity(),executionPrice,cashValue);
		
		return trade;
		
	}

	
	private Trade carrySellTransaction(Order order) throws Exception {
		// TODO Auto-generated method stub
		Portfolio portfolio=portfolioService.getPortfolioForAuserFromPortfolioId(order.getPortfolioId());
		
		BigDecimal executionPrice=order.getTargetPrice().multiply(BigDecimal.valueOf(order.getQuantity()));
		BigDecimal cashValue=executionPrice.add(BigDecimal.valueOf(3));
		
		if(!portfolio.checkSellEligibility(order)) {
			throw new IneligibleOrderException("Portfolio not allowed to do sumit order");
		}
		Trade trade=new Trade(UUID.randomUUID().toString(),order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(),LocalDateTime.now(),order.getQuantity(),executionPrice,cashValue);	
		
		return trade; 
		
	}

	
}
