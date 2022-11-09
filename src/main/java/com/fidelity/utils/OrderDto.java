package com.fidelity.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fidelity.models.Order;

public class OrderDto {

	private String orderId;
	private String direction;
	private BigInteger clientId;
	private String portfolioId;
	private String instrumentId;
	private Integer quantity;
	private BigInteger token;
	private String email;
	private BigDecimal targetPrice;
	public OrderDto(String orderId, String direction, BigInteger clientId, String portfolioId, String instrumentId,
			Integer quantity, BigInteger token, String email, BigDecimal targetPrice) {
		super();
		this.orderId = orderId;
		this.direction = direction;
		this.clientId = clientId;
		this.portfolioId = portfolioId;
		this.instrumentId = instrumentId;
		this.quantity = quantity;
		this.token = token;
		this.email = email;
		this.targetPrice = targetPrice;
	}
	
	public BigDecimal getTargetPrice() {
		return targetPrice;
	}

	public void setTargetPrice(BigDecimal targetPrice) {
		this.targetPrice = targetPrice;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public BigInteger getClientId() {
		return clientId;
	}
	public void setClientId(BigInteger clientId) {
		this.clientId = clientId;
	}
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getInstrumentId() {
		return instrumentId;
	}
	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigInteger getToken() {
		return token;
	}
	public void setToken(BigInteger token) {
		this.token = token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public OrderDto() {
		super();
	}
	
	
	
	
	
	
}
