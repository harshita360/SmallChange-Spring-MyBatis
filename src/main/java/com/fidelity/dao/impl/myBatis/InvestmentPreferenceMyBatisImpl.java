package com.fidelity.dao.impl.myBatis;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.dao.InvestmentPreferenceDao;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.mappers.InvestmentPreferenceMapper;
import com.fidelity.models.InvestmentPreference;

@Component
public class InvestmentPreferenceMyBatisImpl extends InvestmentPreferenceDao{

	@Autowired
	InvestmentPreferenceMapper mapper;
	@Override
	public InvestmentPreference getExistingPref(BigInteger clientId) {
		// TODO Auto-generated method stub
		return mapper.getExistingPref(clientId);
	}

	@Override
	public InvestmentPreference updatePref(InvestmentPreference pref) {
		// TODO Auto-generated method stub
		int res=mapper.updatePref(pref);
		if (res == 0 ) {
			throw new DatabaseException("Could not update Preference");
		}
		
		return pref;
		
		
	}

	@Override
	public InvestmentPreference addNewInvestmentPreference(InvestmentPreference pref) {
		// TODO Auto-generated method stub
		int res= mapper.addNewInvestmentPreference(pref);
		if (res == 0 ) {
			throw new DatabaseException("Could not add Preference");
		}
		return pref;
	}

}
