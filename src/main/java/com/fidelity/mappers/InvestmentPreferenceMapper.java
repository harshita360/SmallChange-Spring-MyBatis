package com.fidelity.mappers;

import java.math.BigInteger;

import com.fidelity.models.InvestmentPreference;

public interface InvestmentPreferenceMapper {

	  InvestmentPreference getExistingPref(BigInteger clientId);
	  int updatePref(InvestmentPreference pref);
	  int addNewInvestmentPreference(InvestmentPreference pref);
}
