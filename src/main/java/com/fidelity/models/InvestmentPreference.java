package com.fidelity.models;

import java.math.BigInteger;
import java.util.Objects;

import com.fidelity.enums.IncomeCategory;
import com.fidelity.enums.LengthOfInvetsment;
import com.fidelity.enums.RiskTolerance;

public class InvestmentPreference {
	private BigInteger clientId;
	private String investmentPurpose;
	private RiskTolerance riskTolerance;
	private IncomeCategory incomeCategory;
	private LengthOfInvetsment lengthOfInvestment;
	public InvestmentPreference(String investmentPurpose, RiskTolerance riskTolerance, IncomeCategory incomeCategory,
			LengthOfInvetsment lengthOfInvestment, BigInteger clientId) {
		super();
		this.investmentPurpose = investmentPurpose;
		this.riskTolerance = riskTolerance;
		this.incomeCategory = incomeCategory;
		this.lengthOfInvestment = lengthOfInvestment;
		this.clientId = clientId;
	}
	public BigInteger getClientId() {
		return clientId;
	}
	public String getInvestmentPurpose() {
		return investmentPurpose;
	}
	public void setInvestmentPurpose(String investmentPurpose) {
		this.investmentPurpose = investmentPurpose;
	}
	public RiskTolerance getRiskTolerance() {
		return riskTolerance;
	}
	public void setRiskTolerance(RiskTolerance riskTolerance) {
		this.riskTolerance = riskTolerance;
	}
	public IncomeCategory getIncomeCategory() {
		return incomeCategory;
	}
	public void setIncomeCategory(IncomeCategory incomeCategory) {
		this.incomeCategory = incomeCategory;
	}
	public LengthOfInvetsment getLengthOfInvestment() {
		return lengthOfInvestment;
	}
	public void setLengthOfInvestment(LengthOfInvetsment lengthOfInvestment) {
		this.lengthOfInvestment = lengthOfInvestment;
	}
	@Override
	public int hashCode() {
		return Objects.hash(incomeCategory, investmentPurpose, lengthOfInvestment, riskTolerance);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvestmentPreference other = (InvestmentPreference) obj;
		return incomeCategory == other.incomeCategory && Objects.equals(investmentPurpose, other.investmentPurpose)
				&& lengthOfInvestment == other.lengthOfInvestment && riskTolerance == other.riskTolerance;
	}
	@Override
	public String toString() {
		return "InvestmentPreference [investmentPurpose=" + investmentPurpose + ", riskTolerance=" + riskTolerance
				+ ", incomeCategory=" + incomeCategory + ", lengthOfInvestment=" + lengthOfInvestment + "]";
	}
	
	public void updatePref(InvestmentPreference pref) {
		this.incomeCategory = pref.incomeCategory;
		this.investmentPurpose = pref.investmentPurpose;
		this.lengthOfInvestment = pref.lengthOfInvestment;
		this.riskTolerance = pref.riskTolerance;
	}
}
