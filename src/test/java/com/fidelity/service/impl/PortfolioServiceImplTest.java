package com.fidelity.service.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fidelity.dao.ClientDao;
import com.fidelity.dao.PortfolioDao;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.models.Client;
import com.fidelity.models.ClientIdentification;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;
import com.fidelity.models.Trade;
import com.fidelity.service.PortfolioService;

@ExtendWith(SpringExtension.class)
@DisplayName("Portfolio Service Test")
@ContextConfiguration("classpath:in-memory-beans.xml")
public class PortfolioServiceImplTest {
	
	@Autowired
	PortfolioDao portfolioDao;
	
	@Autowired
	ClientDao clientDao;
	BigInteger clientId;
	String portfolioId;
	
	@Autowired
	PortfolioService service;
	
	@BeforeEach
	public void setUp() {
		portfolioId=UUID.randomUUID().toString();
		clientId=BigInteger.valueOf(425345345);
		Client client=new Client(clientId,"Nikhil V","nikhil@gmail.com","nikhil123",
				"560061","IN",LocalDate.of(1999, 11, 9),null,new ClientIdentification[] { new ClientIdentification("PAN", "BNHGR76OYA") },"AGGRESSIVE");
		System.out.println(clientDao.registerNewUser(client));
		
	}
	
	@DisplayName("Should add portfolio to repository")
	@Test
	public void testAddPortfolio() {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		holdings.add(holding);
		Portfolio portfolio=new Portfolio(portfolioId,clientId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		service.addNewPortfolio(portfolio);
		
		assertEquals(service.getPortfoliosForAUser(clientId).size(), 1);
		assertEquals(service.getPortfolioForAuserFromPortfolioId(portfolioId), portfolio);
		
	}
	
	@DisplayName("Should get portfolio and mentioned details of instrument holdings")
	@Test
	public void testRetrivePortfolio() throws Exception {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		holdings.add(holding);
		Portfolio portfolio=new Portfolio(portfolioId,clientId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		service.addNewPortfolio(portfolio);
		
		assertEquals(this.portfolioDao.getPortfolioFromIdAndLoadOfInstrument(portfolioId, "TSL").getHoldings().size(),1);
		assertEquals(this.portfolioDao.getPortfolioFromIdAndLoadOfInstrument(portfolioId, "HPQ").getHoldings().size(),0);
		
		
	}
	
	@Test
	@DisplayName("should return null for non existin prtfolio")
	public void testForNonExistingPortfolio() {
		assertNull(portfolioDao.getPortfolioForAuserFromPortfolioId("YGIUIHO"));
	}
	
	@Test
	@DisplayName("should delete existing portfolio from its id")
	public void deletePortfolioFromItsId() throws Exception {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		holdings.add(holding);
		Portfolio portfolio=new Portfolio(portfolioId,clientId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		service.addNewPortfolio(portfolio);
		
		this.service.deletePortfolioById(portfolioId);
		
		assertThrows(Exception.class,()->{
			this.service.getPortfolioForAuserFromPortfolioId(portfolioId);
		});

	}
	
	@Test
	@DisplayName("should throw error on deleting non existing portfolio")
	public void deletePortfolioFromItsIdError() throws Exception {
		
		Exception e=assertThrows(DatabaseException.class, ()->{
			this.service.deletePortfolioById(portfolioId);
		});
		
		assertEquals(e.getMessage(),"Portfolio Not Present");
		
	}
	
	@Test
	@DisplayName("should update the portfolio of an instrument operation")
	public void updatePortfolioFromItsSingleHolding() throws Exception {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(1000.544),now,now);
		holdings.add(holding);
		Portfolio portfolio=new Portfolio(portfolioId,clientId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		service.addNewPortfolio(portfolio);
				
		List<PortfolioHoldings> holdingsU=new ArrayList<>();
		PortfolioHoldings holdingUpdated=new PortfolioHoldings("TSL",BigInteger.valueOf(8),new BigDecimal(803.544),now,now);
		holdingsU.add(holdingUpdated);
		Portfolio portfolioU=new Portfolio(portfolioId,clientId,"Brokerage",
				new BigDecimal(10197),"Brokerage Portfolio",holdingsU);
		
		
		Order order=new Order("UUTT789","S",clientId,portfolioId,"TSL",2,new BigDecimal(100));
		Trade trade=new Trade("TRADE_ID-1",order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(), null,order.getQuantity(),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())),order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())).subtract(new BigDecimal(3)) );
		
		
		service.updatePortfolioFromTrade(trade);
		assertEquals(this.service.getPortfolioForAuserFromPortfolioId(portfolioId),portfolioU);
		
	}
	
	@Test
	@DisplayName("should update the portfolio after selling total quantity of an instrument")
	public void updatePortfolioFromItsSingleHoldinngSellAll() throws Exception {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		PortfolioHoldings holding=new PortfolioHoldings("TSL",BigInteger.valueOf(10),new BigDecimal(100),now,now);
		holdings.add(holding);
		Portfolio portfolio=new Portfolio(portfolioId,clientId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		service.addNewPortfolio(portfolio);
		
		List<PortfolioHoldings> holdingsU=new ArrayList<>();
		Portfolio portfolioU=new Portfolio(portfolioId,clientId,"Brokerage",
				new BigDecimal(10997),"Brokerage Portfolio",holdingsU);
		
		Order order=new Order("UUTT789","S",clientId,portfolioId,"TSL",10,new BigDecimal(100));
		Trade trade=new Trade("TRADE_ID-1",order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(), null,order.getQuantity(),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())),order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())).subtract(new BigDecimal(3)) );
		
		
		
		service.updatePortfolioFromTrade(trade);
		assertEquals(this.portfolioDao.getPortfolioForAuserFromPortfolioId(portfolioId),portfolioU);
		
	}
	
	@Test
	@DisplayName("should update the portfolio after buying a repeat  instrument")
	public void updatePortfolioFromItsSingleHoldinngNewBuyInstrument() throws Exception {
		LocalDateTime now=LocalDateTime.now();
		List<PortfolioHoldings> holdings=new ArrayList<>();
		Portfolio portfolio=new Portfolio(portfolioId,clientId,"Brokerage",
				new BigDecimal(10000),"Brokerage Portfolio",holdings);
		
		service.addNewPortfolio(portfolio);
		
		List<PortfolioHoldings> holdingsU=new ArrayList<>();
		PortfolioHoldings holdingUpdated=new PortfolioHoldings("TSL",BigInteger.valueOf(2),new BigDecimal(203),now,now);
		holdingsU.add(holdingUpdated);
		Portfolio portfolioU=new Portfolio(portfolioId,clientId,"Brokerage",
				new BigDecimal(9797),"Brokerage Portfolio",holdingsU);
		
		Order order=new Order("UUTT789","B",clientId,portfolioId,"TSL",2,new BigDecimal(100));
		Trade trade=new Trade("TRADE_ID-1",order.getDirection(),order,order.getClientId(),
				order.getPortfolioId(),order.getInstrumentId(), null,order.getQuantity(),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())),order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())).add(new BigDecimal(3)) );
		
		
		service.updatePortfolioFromTrade(trade);
		assertEquals(this.portfolioDao.getPortfolioForAuserFromPortfolioId(portfolioId),portfolioU);
		
	}
	
	@AfterEach
	public void teadDown() {
		portfolioDao.deletePortfolioByClientId(clientId);
		clientDao.removeUserById(clientId);
	}
}
