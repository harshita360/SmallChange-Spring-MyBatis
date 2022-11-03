package com.fidelity.service.impl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fidelity.dao.InvestmentPreferenceDao;
import com.fidelity.models.InvestmentPreference;
import com.fidelity.security.JwtTokenService;
import com.fidelity.service.ClientService;
import com.fidelity.service.InvestmentPreferenceService;

@Service
public class InvestmentPreferenceServiceImpl extends InvestmentPreferenceService{
	
	@Autowired
	InvestmentPreferenceDao ifdao;
	
	@Autowired
	private JwtTokenService jwtService;
	

	@Override
	public InvestmentPreference updateInvestmentPref(InvestmentPreference i,String token) {
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





	@Override
	public InvestmentPreference getInvestmentPref(String token) {
		// TODO Auto-generated method stub
InvestmentPreference i=null;
		
		try
		{
		i=ifdao.getExistingPref(new BigInteger(jwtService.extractClientId(token)));
		
		}
		catch(Exception e)
		{
			//rais exception
		}
		return i;
	}

}
