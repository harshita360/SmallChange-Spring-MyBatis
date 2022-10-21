package com.fidelity.service.proxy.myBatis;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.service.PortfolioService;

@Service("portfolioSericeProxyMyBatis")
public class PortfolioServiceMyBatisProxy implements PortfolioService {
	
	private PortfolioService service;
	
	
	@Autowired
	public PortfolioServiceMyBatisProxy(
			@Qualifier("mainPortfolioService") PortfolioService service) {
		super();
		this.service = service;
	}

	@Override
	@Transactional
	public List<Portfolio> getPortfoliosForAUser(BigInteger clientId) {
		return service.getPortfoliosForAUser(clientId);
	}

	@Override
	@Transactional
	public Portfolio getPortfolioForAuserFromPortfolioId(String portfolioId) {
		return service.getPortfolioForAuserFromPortfolioId(portfolioId);
	}

	@Override
	@Transactional
	public Portfolio addNewPortfolio(Portfolio portfolio) {
			return service.addNewPortfolio(portfolio);
	}

	@Override
	@Transactional
	public void deletePortfolioById(String portfolioID) {
		service.deletePortfolioById(portfolioID);
	}

	@Override
	@Transactional
	public void deletePortfolioByClientId(BigInteger clientId) {
		service.deletePortfolioByClientId(clientId);
		
	}

	@Override
	@Transactional
	public Portfolio updatePortfolioFromTrade(Trade trade) {
		return service.updatePortfolioFromTrade(trade);

	}

}
