package com.fidelity.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import com.fidelity.exceptions.IneligibleOrderException;

public class Order {
	private String orderId;
	private String direction;
	private BigInteger clientId;
	private String portfolioId;
	private String instrumentId;
	public Order(String orderId, String direction, BigInteger clientId, String portfolioId, String instrumentId,
			Integer quantity, BigDecimal targetPrice)  {
		super();
		if(!(direction.equals("B")|| (direction.equals("S"))))
		{
			throw new IneligibleOrderException("The order does not have a valid direction");
		}
		this.orderId = orderId;
		this.direction = direction;
		this.clientId = clientId;
		this.portfolioId = portfolioId;
		this.instrumentId = instrumentId;
		this.quantity = quantity;
		this.targetPrice = targetPrice;
	}




	private Integer quantity;
	private BigDecimal targetPrice;

	
	
	
	
	public String getInstrumentId() {
		return instrumentId;
	}




	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}




	@Override
	public int hashCode() {
		return Objects.hash(clientId, direction, instrumentId, orderId, portfolioId, quantity, targetPrice);
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(clientId, other.clientId) && Objects.equals(direction, other.direction)
				&& Objects.equals(instrumentId, other.instrumentId) && Objects.equals(orderId, other.orderId)
				&& Objects.equals(portfolioId, other.portfolioId) && Objects.equals(quantity, other.quantity)
				&& Objects.equals(targetPrice, other.targetPrice);
	}




	public Order() {
		super();
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








	public Integer getQuantity() {
		return quantity;
	}




	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}




	public BigDecimal getTargetPrice() {
		return targetPrice;
	}




	public void setTargetPrice(BigDecimal targetPrice) {
		this.targetPrice = targetPrice;
	}




	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", direction=" + direction + ", clientId=" + clientId + ", portfolioId="
				+ portfolioId + ", instrumentId=" + instrumentId + ", quantity=" + quantity + ", targetPrice="
				+ targetPrice + "]";
	}
	
	
}
