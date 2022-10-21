package com.fidelity.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

public class PortfolioHoldings {
	
	private String insrumentId;
	private BigInteger quantity;
	public PortfolioHoldings() {
		super();
	}
	private BigDecimal invetsmentprice;
	private LocalDateTime lastUpdateAt;
	private LocalDateTime addedAt;
	public String getInsrumentId() {
		return insrumentId;
	}
	public void setInsrumentId(String insrumentId) {
		this.insrumentId = insrumentId;
	}
	public BigInteger getQuantity() {
		return quantity;
	}
	public void setQuantity(BigInteger quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getInvetsmentprice() {
		return invetsmentprice;
	}
	public void setInvetsmentprice(BigDecimal invetsmentprice) {
		this.invetsmentprice = invetsmentprice;
	}
	public LocalDateTime getLastUpdateAt() {
		return lastUpdateAt;
	}
	public void setLastUpdateAt(LocalDateTime lastUpdateAt) {
		this.lastUpdateAt = lastUpdateAt;
	}
	public LocalDateTime getAddedAt() {
		return addedAt;
	}
	public void setAddedAt(LocalDateTime addedAt) {
		this.addedAt = addedAt;
	}
	public PortfolioHoldings(String insrumentId, BigInteger integer, BigDecimal invetsmentprice,
			LocalDateTime lastUpdateAt, LocalDateTime addedAt) {
		super();
		this.insrumentId = insrumentId;
		this.quantity = integer;
		this.invetsmentprice = invetsmentprice;
		this.lastUpdateAt = lastUpdateAt;
		this.addedAt = addedAt;
	}

	public PortfolioHoldings(PortfolioHoldings hold) {
		this.insrumentId = hold.insrumentId;
		this.quantity = hold.quantity;
		this.invetsmentprice = hold.invetsmentprice;
		this.lastUpdateAt = hold.lastUpdateAt;
		this.addedAt = hold.addedAt;
	}
	@Override
	public int hashCode() {
		return Objects.hash(addedAt, insrumentId, invetsmentprice, lastUpdateAt, quantity);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PortfolioHoldings other = (PortfolioHoldings) obj;
		return  Objects.equals(insrumentId, other.insrumentId)
				&& Objects.equals(invetsmentprice, other.invetsmentprice)
				 && Objects.equals(quantity, other.quantity);
	}
	@Override
	public String toString() {
		return "PortfolioHoldings [insrumentId=" + insrumentId + ", quantity=" + quantity + ", invetsmentprice="
				+ invetsmentprice + ", lastUpdateAt=" + lastUpdateAt + ", addedAt=" + addedAt + "]";
	}
	
	

}
