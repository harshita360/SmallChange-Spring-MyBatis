package com.fidelity.dao;

import java.math.BigInteger;
import java.util.List;

import com.fidelity.models.Portfolio;

public abstract class PortfolioDao {
	
//	public static PortfolioDao getInstance(Implementations implementation,ResourceType type) throws Exception {
//		switch(implementation) {
//		case IN_MEM: return PortfolioDaoInMemImpl.getInstance(type);
//		default: throw new Exception("Invalid Choice");
//		}
//	}
	
	public abstract List<Portfolio> getPortfoliosForAUser(BigInteger clientId);
	public abstract Portfolio getPortfolioForAuserFromPortfolioId(String portfolioId);
	public abstract Portfolio addNewPortfolio(Portfolio portfolio);
	public abstract void deletePortfolioById(String portfolioID);
	public abstract void deletePortfolioByClientId(BigInteger clientId);
	public abstract Portfolio getPortfolioFromIdAndLoadOfInstrument(String portfolioId,String instrumentId) ;
	public abstract Portfolio updatePortfolioFromIdAndLoadOfInstrument(Portfolio portfolio,String instrumentId);

}
