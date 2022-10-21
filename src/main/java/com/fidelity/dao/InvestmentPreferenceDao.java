package com.fidelity.dao;

import java.math.BigInteger;

import com.fidelity.models.InvestmentPreference;

public abstract class InvestmentPreferenceDao {

	
	public abstract InvestmentPreference getExistingPref(BigInteger clientId);
	public abstract InvestmentPreference updatePref(InvestmentPreference pref);
	public abstract InvestmentPreference addNewInvestmentPreference(InvestmentPreference pref);
}
