package com.fidelity.service.proxy.myBatis;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.models.Order;
import com.fidelity.models.Trade;
import com.fidelity.service.ActivityService;

@Profile("my-batis")
@Service("proxyActivityService")
public class ActivityServiceMyBatisProxy implements ActivityService{
	
	private ActivityService service;
	
	@Autowired
	public ActivityServiceMyBatisProxy(
			@Qualifier("mainActivityService") ActivityService service) {
		super();
		this.service = service;
	}

	@Override
	@Transactional
	public void addActivity(Trade trade) {
		service.addActivity(trade);
		
	}

	@Override
	@Transactional
	public void addOrder(Order order) {
		service.addOrder(order);
		
	}

	@Override
	@Transactional
	public List<Trade> getUserActivity(BigInteger userId) {
		return service.getUserActivity(userId);
	}

	@Override
	@Transactional
	public List<Trade> getPortfolioActivity(String portfolioId) {
		return service.getPortfolioActivity(portfolioId);
	}

	@Override
	@Transactional
	public void deleteActivityClientId(BigInteger clientId) {
		service.deleteActivityClientId(clientId);
		
	}

	@Override
	@Transactional
	public void deleteActivityPortfolioId(String portfolioId) {
		service.deleteActivityPortfolioId(portfolioId);
		
	}

	@Override
	@Transactional
	public void deleteOrderClientId(BigInteger clientId) {
		service.deleteOrderClientId(clientId);
		
	}

	@Override
	@Transactional
	public void deleteOrderPortfolioId(String portfolioId) {
		service.deleteOrderPortfolioId(portfolioId);
		
	}

}
