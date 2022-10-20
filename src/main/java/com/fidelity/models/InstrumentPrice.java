package com.fidelity.models;

import java.math.BigDecimal;
import java.util.Date;

public class InstrumentPrice {
	BigDecimal askPrice;
	BigDecimal bidPrice;
	Date priceTimestamp;
	Instrument instrument;
	
	
	public InstrumentPrice(BigDecimal askPrice, BigDecimal bidPrice, Date priceTimestamp, Instrument instrument) {
		super();
		this.askPrice = askPrice;
		this.bidPrice = bidPrice;
		this.priceTimestamp = priceTimestamp;
		this.instrument = instrument;
	}
	public BigDecimal getAskPrice() {
		return askPrice;
	}
	public void setAskPrice(BigDecimal askPrice) {
		this.askPrice = askPrice;
	}
	public BigDecimal getBidPrice() {
		return bidPrice;
	}
	public void setBidPrice(BigDecimal bidPrice) {
		this.bidPrice = bidPrice;
	}
	public Date getPriceTimestamp() {
		return priceTimestamp;
	}
	public void setPriceTimestamp(Date priceTimestamp) {
		this.priceTimestamp = priceTimestamp;
	}
	public Instrument getInstrument() {
		return instrument;
	}
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
	
	
}
