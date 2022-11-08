package com.fidelity.service;

import java.math.BigInteger;
import java.util.List;

import com.fidelity.models.Order;
import com.fidelity.models.Trade;

public interface ActivityService {
	public abstract void addActivity(Trade trade);
	public abstract void addOrder(Order order);
	public abstract List<Trade>  getUserActivity(BigInteger userId);
	public abstract List<Trade>  getPortfolioActivity(String portfolioId);
	public abstract void deleteActivityClientId(BigInteger clientId);
	public abstract void deleteActivityPortfolioId(String portfolioId);
	public abstract void deleteOrderClientId(BigInteger clientId);
	public abstract void deleteOrderPortfolioId(String portfolioId);

}
