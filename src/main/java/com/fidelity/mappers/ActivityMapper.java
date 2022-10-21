package com.fidelity.mappers;

import java.math.BigInteger;
import java.util.List;

import com.fidelity.models.Order;
import com.fidelity.models.Trade;

public interface ActivityMapper {
	int addActivity(Trade trade);
	int addOrder(Order order);
	List<Trade>  getUserActivity(BigInteger userId);
	List<Trade>  getPortfolioActivity(String portfolioId);
	int deleteActivityClientId(BigInteger clientId);
	int deleteActivityPortfolioId(String portfolioId);
	int deleteOrderClientId(BigInteger clientId);
	int deleteOrderPortfolioId(String portfolioId);

}
