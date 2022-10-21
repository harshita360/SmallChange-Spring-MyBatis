package com.fidelity.service.proxy;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.fidelity.exceptions.DatabaseException;
import com.fidelity.integration.TransactionManager;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;
import com.fidelity.models.Trade;
import com.fidelity.service.PortfolioService;
import com.fidelity.service.proxy.oracle.PortfolioServiceOracleProxy;

@DisplayName("Portfolio Service proxy test")
public class PortfolioServiceProxyTest {

	PortfolioService proxy,service;
	TransactionManager transManager;

	@BeforeEach
	public void setUp() {
		service = mock(PortfolioService.class);
		transManager = mock(TransactionManager.class);

		proxy = new PortfolioServiceOracleProxy(transManager, service);

	}

	@Test
	public void testToInsertNewPortfolio() {
		LocalDateTime now = LocalDateTime.now();
		List<PortfolioHoldings> holdings = new ArrayList<>();
		PortfolioHoldings holding = new PortfolioHoldings("TSL", BigInteger.valueOf(10), new BigDecimal(1000.544), now,
				now);
		holdings.add(holding);
		Portfolio portfolio = new Portfolio(UUID.randomUUID().toString(), BigInteger.valueOf(100000), "Brokerage",
				new BigDecimal(10000), "Brokerage Portfolio", holdings);

		proxy.addNewPortfolio(portfolio);

		verify(transManager).startTransaction();
		verify(service).addNewPortfolio(portfolio);
		verify(transManager).commitTransaction();

		InOrder order = Mockito.inOrder(transManager, service);
		order.verify(transManager).startTransaction();
		order.verify(service).addNewPortfolio(portfolio);
		order.verify(transManager).commitTransaction();
	}

	@Test
	public void testToInsertNewPortfolioWithException() {
		LocalDateTime now = LocalDateTime.now();
		List<PortfolioHoldings> holdings = new ArrayList<>();
		PortfolioHoldings holding = new PortfolioHoldings("TSL", BigInteger.valueOf(10), new BigDecimal(1000.544), now,
				now);
		holdings.add(holding);
		Portfolio portfolio = new Portfolio(UUID.randomUUID().toString(), BigInteger.valueOf(100000), "Brokerage",
				new BigDecimal(10000), "Brokerage Portfolio", holdings);
		when(service.addNewPortfolio(portfolio)).thenThrow(new DatabaseException());

		assertThrows(DatabaseException.class, () -> {
			proxy.addNewPortfolio(portfolio);
		});

		verify(transManager).startTransaction();
		verify(service).addNewPortfolio(portfolio);
		verify(transManager).rollbackTransaction();

		InOrder order = Mockito.inOrder(transManager, service);
		order.verify(transManager).startTransaction();
		order.verify(service).addNewPortfolio(portfolio);
		order.verify(transManager).rollbackTransaction();
	}

	@Test
	public void tetsToGetPortfolioByPortfolioId() {
		String portfolioId = UUID.randomUUID().toString();
		proxy.getPortfolioForAuserFromPortfolioId(portfolioId);
		verify(service).getPortfolioForAuserFromPortfolioId(portfolioId);
	}

	@Test
	public void testToGetPortfoliosForAUser() {
		BigInteger clientId = BigInteger.valueOf(4535435);
		proxy.getPortfoliosForAUser(clientId);
		verify(service).getPortfoliosForAUser(clientId);
	}

	@Test
	public void deleteAllUserPortfoliosSuccessful() {

		BigInteger clientId = BigInteger.valueOf(4535435);
		proxy.deletePortfolioByClientId(clientId);

		verify(transManager).startTransaction();
		verify(service).deletePortfolioByClientId(clientId);
		verify(transManager).commitTransaction();

		InOrder order = Mockito.inOrder(transManager, service);
		order.verify(transManager).startTransaction();
		order.verify(service).deletePortfolioByClientId(clientId);
		order.verify(transManager).commitTransaction();
	}

	@Test
	public void deleteAllUserPortfoliosUnSuccessful() {

		BigInteger clientId = BigInteger.valueOf(4535435);

		// mocking the method
		doThrow(new DatabaseException("Exception")).when(service).deletePortfolioByClientId(clientId);

		// calling the service method from proxy
		assertThrows(Exception.class, () -> {
			proxy.deletePortfolioByClientId(clientId);
		});

		verify(transManager).startTransaction();
		verify(service).deletePortfolioByClientId(clientId);
		verify(transManager).rollbackTransaction();

		InOrder order = Mockito.inOrder(transManager, service);
		order.verify(transManager).startTransaction();
		order.verify(service).deletePortfolioByClientId(clientId);
		order.verify(transManager).rollbackTransaction();
	}

	@Test
	public void deleteportfolioByIdSuccessful() {

		String portfolioId = UUID.randomUUID().toString();
		proxy.deletePortfolioById(portfolioId);

		verify(transManager).startTransaction();
		verify(service).deletePortfolioById(portfolioId);
		verify(transManager).commitTransaction();

		InOrder order = Mockito.inOrder(transManager, service);
		order.verify(transManager).startTransaction();
		order.verify(service).deletePortfolioById(portfolioId);
		order.verify(transManager).commitTransaction();
	}

	@Test
	public void deleteportfolioByIdUnSuccessful() {

		String portfolioId = UUID.randomUUID().toString();

		// mocking the method
		doThrow(new DatabaseException("Exception")).when(service).deletePortfolioById(portfolioId);
		;
		// calling the service method from proxy
		assertThrows(Exception.class, () -> {
			proxy.deletePortfolioById(portfolioId);
		});

		verify(transManager).startTransaction();
		verify(service).deletePortfolioById(portfolioId);
		verify(transManager).rollbackTransaction();

		InOrder order = Mockito.inOrder(transManager, service);
		order.verify(transManager).startTransaction();
		order.verify(service).deletePortfolioById(portfolioId);
		order.verify(transManager).rollbackTransaction();
	}

	@Test
	public void updatePortfolioFromItsTrade() {
		Order order = new Order("UUTT789", "S", BigInteger.valueOf(3464365), UUID.randomUUID().toString(), "TSL", 10,
				new BigDecimal(100));
		Trade trade = new Trade("TRADE_ID-1", order.getDirection(), order, order.getClientId(), order.getPortfolioId(),
				order.getInstrumentId(), null, order.getQuantity(),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())).subtract(new BigDecimal(3)));

		proxy.updatePortfolioFromTrade(trade);

		verify(transManager).startTransaction();
		verify(service).updatePortfolioFromTrade(trade);
		verify(transManager).commitTransaction();

		InOrder inOrder = Mockito.inOrder(transManager, service);
		inOrder.verify(transManager).startTransaction();
		inOrder.verify(service).updatePortfolioFromTrade(trade);
		inOrder.verify(transManager).commitTransaction();

	}
	
	@Test
	public void updatePortfolioFromItsTradeUnSucessful() {
		Order order = new Order("UUTT789", "S", BigInteger.valueOf(3464365), UUID.randomUUID().toString(), "TSL", 10,
				new BigDecimal(100));
		Trade trade = new Trade("TRADE_ID-1", order.getDirection(), order, order.getClientId(), order.getPortfolioId(),
				order.getInstrumentId(), null, order.getQuantity(),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())).subtract(new BigDecimal(3)));

		when(service.updatePortfolioFromTrade(trade)).thenThrow(new DatabaseException("Update error"));
		
		assertThrows(DatabaseException.class,()->{
			proxy.updatePortfolioFromTrade(trade);
		});
		

		verify(transManager).startTransaction();
		verify(service).updatePortfolioFromTrade(trade);
		verify(transManager).rollbackTransaction();

		InOrder inOrder = Mockito.inOrder(transManager, service);
		inOrder.verify(transManager).startTransaction();
		inOrder.verify(service).updatePortfolioFromTrade(trade);
		inOrder.verify(transManager).rollbackTransaction();

	}

}
