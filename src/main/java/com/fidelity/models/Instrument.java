package com.fidelity.models;

import java.util.Objects;

public class Instrument {
	private String instrumentId;
	private String instrumentDescription;
	private int minQuantity;
	private int maxQuantity;
	private String externalIdType;
	private String externalId;
	private String categoryId;
	
	
	
	
	public Instrument(String instrumentId, String instrumentDescription, int minQuantity, int maxQuantity,
			String externalIdType, String externalId, String categoryId) {
		super();
		this.instrumentId = instrumentId;
		this.instrumentDescription = instrumentDescription;
		this.minQuantity = minQuantity;
		this.maxQuantity = maxQuantity;
		this.externalIdType = externalIdType;
		this.externalId = externalId;
		this.categoryId = categoryId;
	}
	public String getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}
	public String getInstrumentDescription() {
		return instrumentDescription;
	}
	public void setInstrumentDescription(String instrumentDescription) {
		this.instrumentDescription = instrumentDescription;
	}
	public int getMinQuantity() {
		return minQuantity;
	}
	public void setMinQuantity(int minQuantity) {
		this.minQuantity = minQuantity;
	}
	public int getMaxQuantity() {
		return maxQuantity;
	}
	public void setMaxQuantity(int maxQuantity) {
		this.maxQuantity = maxQuantity;
	}
	public String getExternalIdType() {
		return externalIdType;
	}
	public void setExternalIdType(String externalIdType) {
		this.externalIdType = externalIdType;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	@Override
	public int hashCode() {
		return Objects.hash(categoryId, externalId, externalIdType, instrumentDescription, instrumentId, maxQuantity,
				minQuantity);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instrument other = (Instrument) obj;
		return Objects.equals(categoryId, other.categoryId) && Objects.equals(externalId, other.externalId)
				&& Objects.equals(externalIdType, other.externalIdType)
				&& Objects.equals(instrumentDescription, other.instrumentDescription)
				&& Objects.equals(instrumentId, other.instrumentId) && maxQuantity == other.maxQuantity
				&& minQuantity == other.minQuantity;
	}
	@Override
	public String toString() {
		return "Instrument [instrumentId=" + instrumentId + ", instrumentDescription=" + instrumentDescription
				+ ", minQuantity=" + minQuantity + ", maxQuantity=" + maxQuantity + ", externalIdType=" + externalIdType
				+ ", externalId=" + externalId + ", categoryId=" + categoryId + "]";
	}
	
	
}
