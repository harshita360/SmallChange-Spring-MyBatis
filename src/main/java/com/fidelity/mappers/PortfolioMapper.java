package com.fidelity.mappers;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.fidelity.models.Portfolio;



public interface PortfolioMapper {
	List<Portfolio> getPortfoliosOfClient(BigInteger clientId);
	Portfolio getPortfolioFromPortfolioId(String portfolioId);
	Portfolio getPortfolioFromPortfolioIdOfInstrument(Map<Object,Object> data);
	int updatePortfolio(Portfolio portfolio);
	int updatePortfolioHolding(Map<Object,Object> data);
	int deletePortfolioHolding(Map<Object,Object> data);
	int addPortfolioHolding(Map<Object,Object> data);
	int deletePortfolioByPortfolioId(String portfolioId);
	int deletePortfoliosOfClient(BigInteger clientId);
	int addNewPortfolio(Portfolio portfolio);
	
	
	
}
