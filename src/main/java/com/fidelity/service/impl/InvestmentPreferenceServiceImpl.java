package com.fidelity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fidelity.dao.InvestmentPreferenceDao;
import com.fidelity.models.InvestmentPreference;
import com.fidelity.service.ClientService;
import com.fidelity.service.InvestmentPreferenceService;

@Service
public class InvestmentPreferenceServiceImpl extends InvestmentPreferenceService{
	
	@Autowired
	InvestmentPreferenceDao ifdao;
	@Autowired
	private ClientService clientservice;
	

	@Override
	public InvestmentPreference getInvestmentPref() {
		// TODO Auto-generated method stub
		InvestmentPreference i=null;
		
		try
		{
		i=ifdao.getExistingPref(clientservice.getLoggedInUser().getClientId());
		
		}
		catch(Exception e)
		{
			//rais exception
		}
		return i;
		
		
		
	}


	@Override
	public InvestmentPreference updateInvestmentPref(InvestmentPreference i) {
		// TODO Auto-generated method stub
		InvestmentPreference ip=null;
		try
		{
			ip=ifdao.updatePref(i);
		}
		catch(Exception e)
		{
			//raise Exception
		}
		return ip;
	}

}
