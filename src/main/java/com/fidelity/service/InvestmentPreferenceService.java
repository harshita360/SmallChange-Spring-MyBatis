package com.fidelity.service;

import com.fidelity.models.InvestmentPreference;

public abstract class InvestmentPreferenceService {

	public abstract InvestmentPreference getInvestmentPref(String token) throws Exception;
	public abstract InvestmentPreference updateInvestmentPref(InvestmentPreference i) throws Exception;
	public abstract InvestmentPreference insertInvestmentPref(InvestmentPreference i)  throws Exception;
}
