package com.fidelity.service;

import com.fidelity.models.InvestmentPreference;

public abstract class InvestmentPreferenceService {

	public abstract InvestmentPreference getInvestmentPref(String token);
	public abstract InvestmentPreference updateInvestmentPref(InvestmentPreference i,String token);
}
