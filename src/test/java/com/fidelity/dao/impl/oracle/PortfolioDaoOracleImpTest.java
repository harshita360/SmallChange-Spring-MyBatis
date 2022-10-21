package com.fidelity.dao.impl.oracle;

//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fidelity.dao.PortfolioDao;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.integration.DbTestUtils;
import com.fidelity.integration.SimpleDataSource;
import com.fidelity.integration.TransactionManager;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;

@ExtendWith(SpringExtension.class)
@DisplayName("Portfolio dao Oracle Implementation")
@ContextConfiguration("classpath:oracle.xml")
public class PortfolioDaoOracleImpTest {
	
	JdbcTemplate jdbcTemplate;
	DbTestUtils dbTestUtils;
	Connection connection;
	
	@Autowired()
	@Qualifier("portfolioDaoOracle")
	PortfolioDao dao;
	
	
	@Autowired
	SimpleDataSource dataSource;
	
	@Autowired
	TransactionManager transManager;
	
	Portfolio portfolio1;
	
	PortfolioHoldings hold1;
	PortfolioHoldings hold2;
	
	Portfolio portfolio2;
	Portfolio newPortfolio;
	
	BigInteger clientId;
	
	@BeforeEach
	public void setUp() {

		connection=dataSource.getConnection();
		
		dbTestUtils=new DbTestUtils(connection);
		
		
		jdbcTemplate=dbTestUtils.initJdbcTemplate();
		
		
		
		clientId=BigInteger.valueOf(346346435);
		
		transManager.startTransaction();
		List<PortfolioHoldings> holdings1=new ArrayList<>();
		hold1=new PortfolioHoldings("Q345", BigInteger.valueOf(10),BigDecimal.valueOf(876.97),LocalDateTime.of(1999, 9, 29, 23, 59, 59),LocalDateTime.of(1999, 9, 29, 23, 59, 59));
		hold2=new PortfolioHoldings("Q347", BigInteger.valueOf(10),BigDecimal.valueOf(876.97),LocalDateTime.of(1999, 9, 29, 23, 59, 59),LocalDateTime.of(1999, 9, 29, 23, 59, 59));
		holdings1.add(hold1);
		holdings1.add(hold2);
		portfolio1=new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454",clientId,"BROKERAGE",BigDecimal.valueOf(10000),"NIKHIL FIRST PORTFOLIO",holdings1);
	
		List<PortfolioHoldings> holdings2=new ArrayList<>();
		portfolio2=new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3121",clientId,"BROKERAGE-B",BigDecimal.valueOf(10000),"NIKHIL Second PORTFOLIO",holdings2);
		newPortfolio=new Portfolio(UUID.randomUUID().toString(),clientId,"BROKERAGE-B",BigDecimal.valueOf(10000),"NIKHIL Second new PORTFOLIO",null);
	}
	
	@Test
	public void retriveAllClientPortfolios() {
		List<Portfolio> portfolios=dao.getPortfoliosForAUser(clientId);
		//System.out.println(portfolios);
		assertEquals(portfolios.size(),countRowsInTableWhere(jdbcTemplate, "PORTFOLIO","client_id="+clientId),"Must retrive all user portfolios");
		assertTrue(portfolios.contains(portfolio1));
		assertTrue(portfolios.contains(portfolio2));
		for(Portfolio p:portfolios) {
			assertEquals(p.getHoldings().size(), countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"+p.getPortfolioId()+"'"),"Must retrive correct holdings");
		}
	}
	
	@Test
	public void retriveAllClientPortfoliosNoPortfolios() {
		List<Portfolio> portfolios=dao.getPortfoliosForAUser(BigInteger.valueOf(1463465354));
		//System.out.println(portfolios);
		assertEquals(portfolios.size(),countRowsInTableWhere(jdbcTemplate, "PORTFOLIO","client_id="+1463465354),"Must retrive all user portfolios");
		
	}
	
	@Test
	public void retrivePortfolioByItsIdPortfolio1() {
		Portfolio retrived1=dao.getPortfolioForAuserFromPortfolioId(portfolio1.getPortfolioId());
		assertEquals(retrived1,portfolio1);
	}
	
	@Test
	public void retrivePortfolioByItsIdPortfolio2() {
		Portfolio retrived=dao.getPortfolioForAuserFromPortfolioId(portfolio2.getPortfolioId());
		assertEquals(retrived,portfolio2);
	}
	
	@Test
	public void retrivePortfolioContainungInstrumentOnlyExisting1() {
		Portfolio retrived=dao.getPortfolioFromIdAndLoadOfInstrument(portfolio1.getPortfolioId(), hold1.getInsrumentId());
		
		assertEquals(retrived.getHoldings().size(),countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"+portfolio1.getPortfolioId()+"' and instrument_id='"+hold1.getInsrumentId()+"'"));
		
		
	}
	
	@Test
	public void retrivePortfolioContainungInstrumentOnlyNotHolding() {
		Portfolio retrived=dao.getPortfolioFromIdAndLoadOfInstrument(portfolio1.getPortfolioId(), "45RFVDER");
		
		assertEquals(retrived.getHoldings().size(),countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"+portfolio1.getPortfolioId()+"' and instrument_id='"+"45RFVDER"+"'"));
	}
	
	@Test
	public void updatePortfolioWithExistingHoldingUpdate() {
		List<PortfolioHoldings> newHoldings1=new ArrayList<>();
		PortfolioHoldings hold1new=new PortfolioHoldings("Q345", BigInteger.valueOf(12),BigDecimal.valueOf(890.97),LocalDateTime.of(2000, 9, 29, 23, 59, 59),LocalDateTime.of(1999, 9, 29, 23, 59, 59));
		newHoldings1.add(hold1new);
		int oldCountHoldingsOfInstrument=countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"+portfolio1.getPortfolioId()+"' and instrument_id='"+hold1new.getInsrumentId()+"'");
		Portfolio newPortfolio1=new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454",clientId,"BROKERAGE",BigDecimal.valueOf(9979),"NIKHIL FIRST PORTFOLIO",newHoldings1);
		dao.updatePortfolioFromIdAndLoadOfInstrument(newPortfolio1, hold1new.getInsrumentId());
		assertEquals(oldCountHoldingsOfInstrument,countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"+portfolio1.getPortfolioId()+"' and instrument_id='"+hold1new.getInsrumentId()+"'"));
		assertEquals(newPortfolio1, dao.getPortfolioFromIdAndLoadOfInstrument(portfolio1.getPortfolioId(), hold1new.getInsrumentId()));
		
	}
	
	@Test
	public void updatePortfolioWithNewHoldingUpdate() {
		List<PortfolioHoldings> newHoldings2=new ArrayList<>();
		PortfolioHoldings holdnew=new PortfolioHoldings("Q345", BigInteger.valueOf(12),BigDecimal.valueOf(890.97),LocalDateTime.of(2000, 9, 29, 23, 59, 59),LocalDateTime.of(1999, 9, 29, 23, 59, 59));
		newHoldings2.add(holdnew);
		int oldCountHoldingsOfInstrument=countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"+portfolio2.getPortfolioId()+"' and instrument_id='"+holdnew.getInsrumentId()+"'");
		Portfolio newPortfolio2=new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3121",clientId,"BROKERAGE-B",BigDecimal.valueOf(10000),"NIKHIL Second PORTFOLIO",newHoldings2);
		dao.updatePortfolioFromIdAndLoadOfInstrument(newPortfolio2, holdnew.getInsrumentId());
		assertEquals(oldCountHoldingsOfInstrument+1,countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"+portfolio1.getPortfolioId()+"' and instrument_id='"+holdnew.getInsrumentId()+"'"));
		assertEquals(newPortfolio2, dao.getPortfolioFromIdAndLoadOfInstrument(portfolio2.getPortfolioId(), holdnew.getInsrumentId()));
		
	}
	
	@Test
	public void updatePortfolioWithRemoveHoldingUpdate() {
		List<PortfolioHoldings> newHoldings1=new ArrayList<>();
		
		int oldCountHoldingsOfInstrument=countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"+portfolio1.getPortfolioId()+"' and instrument_id='"+hold1.getInsrumentId()+"'");
		Portfolio newPortfolio1=new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454",clientId,"BROKERAGE",BigDecimal.valueOf(1200),"NIKHIL FIRST PORTFOLIO",newHoldings1);
		dao.updatePortfolioFromIdAndLoadOfInstrument(newPortfolio1, hold1.getInsrumentId());
		assertEquals(oldCountHoldingsOfInstrument-1,countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"+portfolio1.getPortfolioId()+"' and instrument_id='"+hold1.getInsrumentId()+"'"));
		assertEquals(newPortfolio1, dao.getPortfolioFromIdAndLoadOfInstrument(portfolio1.getPortfolioId(), hold1.getInsrumentId()));
		
	}
	
	@Test
	public void updatePortfolioMustThrowExceptionOnUpdatingNonExistingPortfolio() {
		assertThrows(DatabaseException.class,()->{
			Portfolio newPortfolio=new Portfolio(UUID.randomUUID().toString(),clientId,"BROKERAGE-B",BigDecimal.valueOf(10000),"NIKHIL Second new PORTFOLIO",null);
			dao.updatePortfolioFromIdAndLoadOfInstrument(newPortfolio, "ABC");
		});
		
	}
	
	@Test
	public void deletePortfolioFromItsId() {
		int oldPortfolioCount=countRowsInTable(jdbcTemplate, "PORTFOLIO");
		int oldHoldingsCount=countRowsInTable(jdbcTemplate, "portfolio_holding");
		
		dao.deletePortfolioById(portfolio1.getPortfolioId());
		
		assertEquals(oldPortfolioCount-1,countRowsInTable(jdbcTemplate, "PORTFOLIO"));
		assertEquals(oldHoldingsCount-2,countRowsInTable(jdbcTemplate, "portfolio_holding"));
		assertNull(dao.getPortfolioForAuserFromPortfolioId(portfolio1.getPortfolioId()));
	}
	
	@Test
	public void deletePortfolioFromItsIdWithNoHoldings() {
		int oldPortfolioCount=countRowsInTable(jdbcTemplate, "PORTFOLIO");
		int oldHoldingsCount=countRowsInTable(jdbcTemplate, "portfolio_holding");
		
		dao.deletePortfolioById(portfolio2.getPortfolioId());
		
		assertEquals(oldPortfolioCount-1,countRowsInTable(jdbcTemplate, "PORTFOLIO"));
		assertEquals(oldHoldingsCount,countRowsInTable(jdbcTemplate, "portfolio_holding"));
		assertNull(dao.getPortfolioForAuserFromPortfolioId(portfolio2.getPortfolioId()));
	}
	
	@Test
	public void deletePortfolioNotExisting() {
		assertThrows(DatabaseException.class,()->{
			dao.deletePortfolioById(UUID.randomUUID().toString());
		});
	}
	
	@Test
	public void deletePortfolioOfClient() {
		int oldPortfolioCount=countRowsInTable(jdbcTemplate, "PORTFOLIO");
		int oldHoldingsCount=countRowsInTable(jdbcTemplate, "portfolio_holding");
		
		dao.deletePortfolioByClientId(clientId);
		
		assertEquals(oldPortfolioCount-2,countRowsInTable(jdbcTemplate, "PORTFOLIO"));
		assertEquals(oldHoldingsCount-2,countRowsInTable(jdbcTemplate, "portfolio_holding"));
		assertEquals(0,dao.getPortfoliosForAUser(clientId).size());
	}
	
	@Test
	public void addnewUserPortfolio() {
		int oldPortfolioCount=countRowsInTable(jdbcTemplate, "PORTFOLIO");
		int oldHoldingsCount=countRowsInTable(jdbcTemplate, "portfolio_holding");
		
		dao.addNewPortfolio(newPortfolio);
		
		assertEquals(oldPortfolioCount+1,countRowsInTable(jdbcTemplate, "PORTFOLIO"));
		assertEquals(oldHoldingsCount,countRowsInTable(jdbcTemplate, "portfolio_holding"));
		assertEquals(1,countRowsInTableWhere(jdbcTemplate, "PORTFOLIO", "PORTFOLIO_ID='"+newPortfolio.getPortfolioId()+"'"));
		assertEquals(0,countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"+newPortfolio.getPortfolioId()+"'"));
		assertEquals(3,dao.getPortfoliosForAUser(clientId).size());
	}
	
	@Test
	public void addnewUserExistingPortfolioMustThrowError() {
		assertThrows(DatabaseException.class,()->{
			dao.addNewPortfolio(portfolio1);
		});
	}
	
	@Test
	public void addnewUserPortfolioOfNonexistingUserMustThrowError() {
		assertThrows(DatabaseException.class,()->{
			Portfolio newPortfolio=new Portfolio(UUID.randomUUID().toString(),BigInteger.valueOf(76876989),"BROKERAGE-B",BigDecimal.valueOf(10000),"NIKHIL Second new PORTFOLIO",null);
			dao.addNewPortfolio(newPortfolio);
		});
	}
	
	@Test
	public void deletePortfolioNotExistingClient() {
		assertThrows(DatabaseException.class,()->{
			dao.deletePortfolioByClientId(BigInteger.valueOf(65869897));
		});
	}
	
	@AfterEach
	public void tearDown() {
		transManager.rollbackTransaction();
	}

}
