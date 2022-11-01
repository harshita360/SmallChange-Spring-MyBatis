package com.fidelity.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fidelity.models.Portfolio;

public class PortfolioUtils {
	
	public static Portfolio getDefaultPortfolio(BigInteger clientId) {
		Portfolio portfolio=new Portfolio();
		portfolio.setBalance(BigDecimal.valueOf(10000));
		portfolio.setClientId(clientId);
		portfolio.setHoldings(null);
		portfolio.setPortfolioName("First Portfolio");
		portfolio.setPortfolioTypeName("BROKERAGE");
		portfolio.setPortfolioId(null);
		return portfolio;
	}

}
