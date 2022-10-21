package com.fidelity.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fidelity.dao.ClientDao;
import com.fidelity.dao.PortfolioDao;
import com.fidelity.exceptions.NotFoundException;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.service.PortfolioService;

@Service("mainPortfolioService")
public class PortfolioServiceImpl implements PortfolioService {
	
	private ClientDao clientdao;
	private PortfolioDao portfolioDao;
	
	@Autowired
	public PortfolioServiceImpl(ClientDao clientdao, PortfolioDao portfolioDao) {
		super();
		this.clientdao = clientdao;
		this.portfolioDao = portfolioDao;
	}
	
	
	@Override
	public List<Portfolio> getPortfoliosForAUser(BigInteger clientId) {
		// check to see if the client exists
			// if not exists, throw exception
		if(clientdao.getUserById(clientId)==null) {
			throw new NotFoundException("Client with id"+clientId+" not found");
		}
		// get the user portfolios
		return portfolioDao.getPortfoliosForAUser(clientId);
	}
	
	
	@Override
	public Portfolio getPortfolioForAuserFromPortfolioId(String portfolioId) {
		// get the client portfolio from its id
		Portfolio portfolio=portfolioDao.getPortfolioForAuserFromPortfolioId(portfolioId);
		/// if the repository returns null
		if(portfolio==null) {
			// throw new NotFounfException
			throw new NotFoundException("Portfolio with id"+portfolioId+" not found");
		}
		// else return the retrieved portfolio
		return portfolio;
	}
	@Override
	public Portfolio addNewPortfolio(Portfolio portfolio) {
		// check to see if the client exists
		if(clientdao.getUserById(portfolio.getClientId())==null) {
			// if not exists, throw exception
			throw new NotFoundException("Client with id"+portfolio.getClientId()+" not found");
		}
		return portfolioDao.addNewPortfolio(portfolio);
	}
	
	
	@Override
	public void deletePortfolioById(String portfolioID) {
		this.portfolioDao.deletePortfolioById(portfolioID);
	}
	@Override
	public void deletePortfolioByClientId(BigInteger clientId) {
		// check to see if the client exists
		if(clientdao.getUserById(clientId)==null) {
			// if not exists, throw exception
			throw new NotFoundException("Client with id"+clientId+" not found");
		}
		// delete the portfolios of the client
		portfolioDao.deletePortfolioByClientId(clientId);
		
	}
	@Override
	public Portfolio updatePortfolioFromTrade(Trade trade) {
		// get the user portfolio of that instrument from the trade data
		Portfolio portfolio=portfolioDao.getPortfolioFromIdAndLoadOfInstrument(trade.getPortfolioId(), trade.getInstrumentId());
		// if there is no portfolio throw exception
		if(portfolio==null) {
			throw new NotFoundException("Portfolio with id"+trade.getPortfolioId()+" not found");
		}
		// update the portfolio from its trade
		portfolio.updateHoldings(trade);
		// call dao to update the portfolio
		Portfolio ret=portfolioDao.updatePortfolioFromIdAndLoadOfInstrument(portfolio, trade.getInstrumentId());
		return ret;
	}
	
	

}
