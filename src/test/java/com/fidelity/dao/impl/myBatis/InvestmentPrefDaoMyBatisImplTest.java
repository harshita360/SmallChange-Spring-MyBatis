package com.fidelity.dao.impl.myBatis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.enums.IncomeCategory;
import com.fidelity.enums.LengthOfInvetsment;
import com.fidelity.enums.RiskTolerance;
import com.fidelity.models.InvestmentPreference;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:beans.xml")
@Transactional
public class InvestmentPrefDaoMyBatisImplTest {

	
	@Autowired
	DataSource dataSource;
	
	JdbcTemplate jdbcTemplate;
	Connection connection;
	BigInteger clientId;
	
	@Autowired
	InvestmentPreferenceMyBatisImpl investmentprefdao;
	
	
	@BeforeEach
	public void setUp() {
        jdbcTemplate=new JdbcTemplate(dataSource);
		clientId=BigInteger.valueOf(346346435);
	}
	
	@Test
	void getUserExisitingPreference() {
		InvestmentPreference ip=investmentprefdao.getExistingPref(clientId);
		assertNotNull(ip);
	}
	
	@Test
	void testUpdatePreference() {
		InvestmentPreference newip=new InvestmentPreference("Tax Saving",RiskTolerance.AVERAGE,IncomeCategory.HIGH,LengthOfInvetsment.BASIC,this.clientId);
	    investmentprefdao.updatePref(newip);
	    InvestmentPreference ip2=investmentprefdao.getExistingPref(clientId);
	    assertEquals(ip2.getRiskTolerance(),RiskTolerance.AVERAGE);
	}
	
	
	@Test
	void testInsertInvestmentPreference()
	{
		InvestmentPreference newip=new InvestmentPreference("Tax Saving",RiskTolerance.AVERAGE,IncomeCategory.HIGH,LengthOfInvetsment.BASIC,new BigInteger("1463465354"));
	    assertNotNull(investmentprefdao.addNewInvestmentPreference(newip));
	    
	}
	
}
