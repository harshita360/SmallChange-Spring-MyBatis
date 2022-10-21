package com.fidelity.service;

import java.math.BigInteger;
import java.util.List;

import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;

public interface PortfolioService {
	
	public  List<Portfolio> getPortfoliosForAUser(BigInteger clientId);
	public  Portfolio getPortfolioForAuserFromPortfolioId(String portfolioId);
	public  Portfolio addNewPortfolio(Portfolio portfolio);
	public  void deletePortfolioById(String portfolioID);
	public  void deletePortfolioByClientId(BigInteger clientId);
	public  Portfolio updatePortfolioFromTrade(Trade trade);

}
