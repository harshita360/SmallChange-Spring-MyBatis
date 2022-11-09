package com.fidelity.dao.impl.myBatis;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.dao.ActivityDao;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.mappers.ActivityMapper;
import com.fidelity.models.Order;
import com.fidelity.models.Trade;

@Component
@Profile("my-batis")
public class ActivityDaoMyBatisImpl extends ActivityDao {
	
	@Autowired
	ActivityMapper mapper;

	@Override
	@Transactional
	public void addActivity(Trade trade) {
		//mapper.addOrder(trade.getOrder());
		int status=mapper.addActivity(trade);
		if(status==0) {
			throw new DatabaseException("Failed to insert activity!!!");
		}
		
	}

	@Override
	@Transactional
	public void addOrder(Order order) {
		int status=mapper.addOrder(order);
		if(status==0) {
			throw new DatabaseException("Failed to insert order!!!");
		}
		
	}

	@Override
	@Transactional
	public List<Trade> getUserActivity(BigInteger userId) {
		return mapper.getUserActivity(userId);
	}

	@Override
	@Transactional
	public List<Trade> getPortfolioActivity(String portfolioId) {
		return mapper.getPortfolioActivity(portfolioId);
	}

	@Override
	@Transactional
	public void deleteActivityClientId(BigInteger clientId) {
		int status=mapper.deleteActivityClientId(clientId);
		if(status==0) {
			throw new DatabaseException("Failed to delete activity!!!");
		}
		mapper.deleteOrderClientId(clientId);
		
	}

	@Override
	@Transactional
	public void deleteActivityPortfolioId(String portfolioId) {
		int status=mapper.deleteActivityPortfolioId(portfolioId);
		if(status==0) {
			throw new DatabaseException("Failed to delete activity!!!");
		}
		mapper.deleteOrderPortfolioId(portfolioId);
		
	}

	@Override
	@Transactional
	public void deleteOrderClientId(BigInteger clientId) {
		int status=mapper.deleteOrderClientId(clientId);
		if(status==0) {
			throw new DatabaseException("Failed to delete order!!!");
		}
		
	}

	@Override
	@Transactional
	public void deleteOrderPortfolioId(String portfolioId) {
		int status=mapper.deleteOrderPortfolioId(portfolioId);
		if(status==0) {
			throw new DatabaseException("Failed to delete order!!!");
		}
		
	}

}
