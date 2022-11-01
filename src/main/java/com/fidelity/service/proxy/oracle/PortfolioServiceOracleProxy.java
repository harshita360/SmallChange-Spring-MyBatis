package com.fidelity.service.proxy.oracle;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.fidelity.exceptions.DatabaseException;
import com.fidelity.integration.TransactionManager;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.service.PortfolioService;

@Service("portfolioSericeProxyOracle")
@Profile("oracle")
public class PortfolioServiceOracleProxy implements PortfolioService {
	
	private TransactionManager transManager;
	private PortfolioService service;
	
	
	@Autowired
	public PortfolioServiceOracleProxy(TransactionManager transManager,
			@Qualifier("mainPortfolioService") PortfolioService service) {
		super();
		this.transManager = transManager;
		this.service = service;
	}

	@Override
	public List<Portfolio> getPortfoliosForAUser(BigInteger clientId) {
		return service.getPortfoliosForAUser(clientId);
	}

	@Override
	public Portfolio getPortfolioForAuserFromPortfolioId(String portfolioId) {
		// TODO Auto-generated method stub
		return service.getPortfolioForAuserFromPortfolioId(portfolioId);
	}

	@Override
	public Portfolio addNewPortfolio(Portfolio portfolio) {
		
		transManager.startTransaction();
		try {
			Portfolio port=service.addNewPortfolio(portfolio);
			transManager.commitTransaction();
			return port;
		}catch(DatabaseException e) {
			transManager.rollbackTransaction();
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void deletePortfolioById(String portfolioID) {
		transManager.startTransaction();
		try {
			service.deletePortfolioById(portfolioID);
			transManager.commitTransaction();
		}catch(DatabaseException e) {
			transManager.rollbackTransaction();
			throw new DatabaseException(e.getMessage());
		}
		
	}

	@Override
	public void deletePortfolioByClientId(BigInteger clientId) {
		transManager.startTransaction();
		try {
			service.deletePortfolioByClientId(clientId);
			transManager.commitTransaction();
		}catch(DatabaseException e) {
			transManager.rollbackTransaction();
			throw new DatabaseException(e.getMessage());
		}
		
	}

	@Override
	public Portfolio updatePortfolioFromTrade(Trade trade) {
		transManager.startTransaction();
		try {
			Portfolio port=service.updatePortfolioFromTrade(trade);
			transManager.commitTransaction();
			return port;
		}catch(DatabaseException e) {
			transManager.rollbackTransaction();
			throw new DatabaseException(e.getMessage());
		}
	}

}
