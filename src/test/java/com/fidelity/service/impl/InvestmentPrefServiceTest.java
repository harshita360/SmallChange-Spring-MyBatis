package com.fidelity.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.models.InvestmentPreference;
import com.fidelity.service.InvestmentPreferenceService;

@SpringBootTest
@Transactional
public class InvestmentPrefServiceTest {
	 @Autowired
	 private InvestmentPreferenceService service;
	    
	    @Autowired
	    private JdbcTemplate jdbcTemplate;
	    
	    @Test
	    void testQueryForAllPreferences() throws Exception {
	        
	        InvestmentPreference responseStatus = service.getInvestmentPref(null);
	        
	     
	    }
	    
	
}
