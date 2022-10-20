package com.fidelity.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

public class Trade {
	private String tradeId;
	private String direction;
	private Order order;
	private BigInteger clientId;
	private String portfolioId;
	private String instrumentId;
	
	//@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	//@JsonSerialize(using = LocalDateTimeSerializer.class)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime transactionAt;
	private Integer quantity;
	private BigDecimal executionPrice;
	private BigDecimal cashValue;
	
	public Trade(String tradeId, String direction, Order order, BigInteger clientId, String portfolioId,
			String instrumentId,LocalDateTime transactionAt, Integer quantity, BigDecimal executionPrice,
			BigDecimal cashValue) {
		super();
		this.tradeId = tradeId;
		this.direction = direction;
		this.order = order;
		this.clientId = clientId;
		this.portfolioId = portfolioId;
		this.instrumentId = instrumentId;
		this.transactionAt = transactionAt;
		this.quantity = quantity;
		this.executionPrice = executionPrice;
		this.cashValue = cashValue;
	}
	public Trade() {
		super();
	}
	@Override
	public int hashCode() {
		return Objects.hash(cashValue, clientId, direction, executionPrice, instrumentId, order, portfolioId, quantity,
				tradeId, transactionAt);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trade other = (Trade) obj;
		return Objects.equals(cashValue, other.cashValue) && Objects.equals(clientId, other.clientId)
				&& Objects.equals(direction, other.direction) && Objects.equals(executionPrice, other.executionPrice)
				&& Objects.equals(instrumentId, other.instrumentId) && Objects.equals(order, other.order)
				&& Objects.equals(portfolioId, other.portfolioId) && Objects.equals(quantity, other.quantity)
				&& Objects.equals(tradeId, other.tradeId) && Objects.equals(transactionAt, other.transactionAt);
	}
	@Override
	public String toString() {
		return "Trade [tradeId=" + tradeId + ", direction=" + direction + ", order=" + order + ", clientId=" + clientId
				+ ", portfolioId=" + portfolioId + ", instrumentId=" + instrumentId + ", transactionAt=" + transactionAt
				+ ", quantity=" + quantity + ", executionPrice=" + executionPrice + ", cashValue=" + cashValue + "]";
	}
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
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
	public LocalDateTime getTransactionAt() {
		return transactionAt;
	}
	public void setTransactionAt(LocalDateTime transactionAt) {
		this.transactionAt = transactionAt;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getExecutionPrice() {
		return executionPrice;
	}
	public void setExecutionPrice(BigDecimal executionPrice) {
		this.executionPrice = executionPrice;
	}
	public BigDecimal getCashValue() {
		return cashValue;
	}
	public void setCashValue(BigDecimal cashValue) {
		this.cashValue = cashValue;
	}
	
}
