package com.fidelity.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fidelity.dao.ActivityDao;
import com.fidelity.dao.ClientDao;
import com.fidelity.dao.PortfolioDao;
import com.fidelity.exceptions.NotFoundException;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;
import com.fidelity.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {
	
	private ClientDao clientDao;
	private PortfolioDao portfolioDao;
	private ActivityDao activityDao;
	
	@Autowired
	public ActivityServiceImpl(ClientDao clientDao,PortfolioDao portfolioDao,ActivityDao activityDao) {
		super();
		this.clientDao=clientDao;
		this.portfolioDao=portfolioDao;
		this.activityDao=activityDao;
	}

	@Override
	public void addActivity(Trade trade) {
		if(clientDao.getUserById(trade.getClientId())==null) {
			throw new NotFoundException(clientErrorMsg(trade.getClientId().toString()));
		}
		//activityDao.addOrder(trade.getOrder());
		activityDao.addActivity(trade);
		
	}
	
	@Override
	public Order addOrder(Order order) {
		order.setOrderId(UUID.randomUUID().toString());
		if(clientDao.getUserById(order.getClientId())==null) {
			throw new NotFoundException(clientErrorMsg(order.getClientId().toString()));
		}
		
		System.out.println(order.getOrderId());
        activityDao.addOrder(order);
		return order;
		
	}

	@Override
	public List<Trade> getUserActivity(BigInteger userId) {
		if(clientDao.getUserById(userId)==null) {
			throw new NotFoundException(clientErrorMsg(userId.toString()));
		}
		return(activityDao.getUserActivity(userId));
	}

	@Override
	public List<Trade> getPortfolioActivity(String portfolioId) {
		Portfolio portfolio = portfolioDao.getPortfolioForAuserFromPortfolioId(portfolioId);
		if(portfolio==null) {
			throw new NotFoundException(portfolioErrorMsg(portfolioId));
		}
		return activityDao.getPortfolioActivity(portfolioId);
	}

	@Override
	public void deleteActivityClientId(BigInteger clientId) {
		if(clientDao.getUserById(clientId)==null) {
			throw new NotFoundException(clientErrorMsg(clientId.toString()));
		}
		activityDao.deleteActivityClientId(clientId);
		
	}

	@Override
	public void deleteActivityPortfolioId(String portfolioId) {
		Portfolio portfolio = portfolioDao.getPortfolioForAuserFromPortfolioId(portfolioId);
		if(portfolio==null) {
			throw new NotFoundException(portfolioErrorMsg(portfolioId));
		}
		activityDao.deleteActivityPortfolioId(portfolioId);
		
	}
	
	@Override
	public void deleteOrderClientId(BigInteger clientId) {
		if(clientDao.getUserById(clientId)==null) {
			throw new NotFoundException(clientErrorMsg(clientId.toString()));
		}
		activityDao.deleteOrderClientId(clientId);
		
	}

	@Override
	public void deleteOrderPortfolioId(String portfolioId) {
		Portfolio portfolio = portfolioDao.getPortfolioForAuserFromPortfolioId(portfolioId);
		if(portfolio==null) {
			throw new NotFoundException(portfolioErrorMsg(portfolioId));
		}
		activityDao.deleteOrderPortfolioId(portfolioId);
		
	}
	
	private String clientErrorMsg(String id) {
		return(String.format("Client with id: %s does not exist!!!",id));
	}
	
	private String portfolioErrorMsg(String id) {
		return(String.format("Portfolio with id: %s does not exist!!!",id));
	}



}
