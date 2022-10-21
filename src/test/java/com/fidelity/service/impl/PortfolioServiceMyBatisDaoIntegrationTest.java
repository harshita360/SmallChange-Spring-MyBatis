package com.fidelity.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.dao.PortfolioDao;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.exceptions.NotFoundException;
import com.fidelity.integration.DbTestUtils;
import com.fidelity.integration.SimpleDataSource;
import com.fidelity.integration.TransactionManager;
import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;
import com.fidelity.models.Trade;
import com.fidelity.service.PortfolioService;

@ExtendWith(SpringExtension.class)
@DisplayName("Portfolio Service Integration MyBatis Test")
@ContextConfiguration("classpath:beans.xml")
@Transactional
public class PortfolioServiceMyBatisDaoIntegrationTest {

	BigInteger clientId;

	@Autowired
	@Qualifier("portfolioSericeProxyMyBatis")
	PortfolioService service;

	JdbcTemplate jdbcTemplate;

	@Autowired
	PortfolioDao dao;

	@Autowired
	DataSource dataSource;

	Portfolio portfolio1;

	PortfolioHoldings hold1;
	PortfolioHoldings hold2;

	Portfolio portfolio2;
	Portfolio newPortfolio;

	@BeforeEach
	public void setUp() {

		jdbcTemplate = new JdbcTemplate(dataSource);

		clientId = BigInteger.valueOf(346346435);

		List<PortfolioHoldings> holdings1 = new ArrayList<>();
		hold1 = new PortfolioHoldings("Q345", BigInteger.valueOf(10), BigDecimal.valueOf(876.97),
				LocalDateTime.of(1999, 9, 29, 23, 59, 59), LocalDateTime.of(1999, 9, 29, 23, 59, 59));
		hold2 = new PortfolioHoldings("Q347", BigInteger.valueOf(10), BigDecimal.valueOf(876.97),
				LocalDateTime.of(1999, 9, 29, 23, 59, 59), LocalDateTime.of(1999, 9, 29, 23, 59, 59));
		holdings1.add(hold1);
		holdings1.add(hold2);
		portfolio1 = new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454", clientId, "BROKERAGE",
				BigDecimal.valueOf(10000), "NIKHIL FIRST PORTFOLIO", holdings1);

		List<PortfolioHoldings> holdings2 = new ArrayList<>();
		portfolio2 = new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3121", clientId, "BROKERAGE-B",
				BigDecimal.valueOf(10000), "NIKHIL Second PORTFOLIO", holdings2);
		newPortfolio = new Portfolio(UUID.randomUUID().toString(), clientId, "BROKERAGE-B", BigDecimal.valueOf(10000),
				"NIKHIL Second new PORTFOLIO", null);

	}

	@Test
	public void retriveAllClientPortfolios() {
		List<Portfolio> portfolios = service.getPortfoliosForAUser(clientId);
		// System.out.println(portfolios);
		assertEquals(portfolios.size(), countRowsInTableWhere(jdbcTemplate, "PORTFOLIO", "client_id=" + clientId),
				"Must retrive all user portfolios");
		assertTrue(portfolios.contains(portfolio1));
		assertTrue(portfolios.contains(portfolio2));
		for (Portfolio p : portfolios) {
			assertEquals(p.getHoldings().size(), countRowsInTableWhere(jdbcTemplate, "portfolio_holding",
					"PORTFOLIO_ID='" + p.getPortfolioId() + "'"), "Must retrive correct holdings");
		}
	}

	@Test
	public void retriveAllClientPortfoliosNoPortfolios() {

		List<Portfolio> portfolios = service.getPortfoliosForAUser(BigInteger.valueOf(1463465354));
		// System.out.println(portfolios);
		assertEquals(portfolios.size(), countRowsInTableWhere(jdbcTemplate, "PORTFOLIO", "client_id=" + 1463465354),
				"Must retrive all user portfolios");

	}

	@Test
	public void retrivePortfolioByItsIdPortfolio1() {
		Portfolio retrived1 = service.getPortfolioForAuserFromPortfolioId(portfolio1.getPortfolioId());
		assertEquals(retrived1, portfolio1, "Must retrive correct portfolio");
	}

	@Test
	public void retrivePortfolioByItsIdPortfolio2() {
		Portfolio retrived = service.getPortfolioForAuserFromPortfolioId(portfolio2.getPortfolioId());
		assertEquals(retrived, portfolio2, "Must retrive correct portfolio");
	}

	@Test
	public void updatePortfolioWithExistingHoldingSellOperation() {

		// balance =10000
		Order order = new Order("UUTT789", "S", portfolio1.getClientId(), portfolio1.getPortfolioId(), "Q345", 2,
				new BigDecimal(100));
		Trade trade = new Trade("TRADE_ID-1", order.getDirection(), order, order.getClientId(), order.getPortfolioId(),
				order.getInstrumentId(), null, order.getQuantity(),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())).subtract(new BigDecimal(3)));

		service.updatePortfolioFromTrade(trade);
		// updated balance = 10000+197 =10197
		// updated investment = 876.97-197=679.97

		List<PortfolioHoldings> newHoldings1 = new ArrayList<>();
		PortfolioHoldings hold1new = new PortfolioHoldings("Q345", BigInteger.valueOf(8), BigDecimal.valueOf(679.97),
				LocalDateTime.of(2000, 9, 29, 23, 59, 59), LocalDateTime.of(1999, 9, 29, 23, 59, 59));
		newHoldings1.add(hold1new);

		int oldCountHoldingsOfInstrument = countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"
				+ portfolio1.getPortfolioId() + "' and instrument_id='" + trade.getInstrumentId() + "'");
		Portfolio newPortfolio1 = new Portfolio(portfolio1.getPortfolioId(), portfolio1.getClientId(),
				portfolio1.getPortfolioTypeName(), BigDecimal.valueOf(10197), portfolio1.getPortfolioName(),
				newHoldings1);

		assertEquals(oldCountHoldingsOfInstrument,
				countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='" + portfolio1.getPortfolioId()
						+ "' and instrument_id='" + hold1new.getInsrumentId() + "'"));
		assertEquals(newPortfolio1,
				dao.getPortfolioFromIdAndLoadOfInstrument(portfolio1.getPortfolioId(), hold1new.getInsrumentId()),
				"Must get updated portfolio alter trade execution");

	}

	@Test
	public void updatePortfolioWithNewHoldingUpdateRepeatBuyOperation() {
		// balance =10000
		Order order = new Order("UUTT789", "B", portfolio1.getClientId(), portfolio1.getPortfolioId(), "Q345", 2,
				new BigDecimal(100));
		Trade trade = new Trade("TRADE_ID-1", order.getDirection(), order, order.getClientId(), order.getPortfolioId(),
				order.getInstrumentId(), null, order.getQuantity(),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())).add(new BigDecimal(3)));

		service.updatePortfolioFromTrade(trade);
		// updated balance = 10000-203 = 9797
		// updated investment = 876.97+203=1079.97

		List<PortfolioHoldings> newHoldings1 = new ArrayList<>();
		PortfolioHoldings hold1new = new PortfolioHoldings("Q345", BigInteger.valueOf(12), BigDecimal.valueOf(1079.97),
				LocalDateTime.of(2000, 9, 29, 23, 59, 59), LocalDateTime.of(1999, 9, 29, 23, 59, 59));
		newHoldings1.add(hold1new);

		int oldCountHoldingsOfInstrument = countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"
				+ portfolio1.getPortfolioId() + "' and instrument_id='" + trade.getInstrumentId() + "'");
		Portfolio newPortfolio1 = new Portfolio(portfolio1.getPortfolioId(), portfolio1.getClientId(),
				portfolio1.getPortfolioTypeName(), BigDecimal.valueOf(9797), portfolio1.getPortfolioName(),
				newHoldings1);

		assertEquals(oldCountHoldingsOfInstrument,
				countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='" + portfolio1.getPortfolioId()
						+ "' and instrument_id='" + hold1new.getInsrumentId() + "'"));
		assertEquals(newPortfolio1,
				dao.getPortfolioFromIdAndLoadOfInstrument(portfolio1.getPortfolioId(), hold1new.getInsrumentId()),
				"Must get updated portfolio alter trade execution");

	}

	@Test
	public void updatePortfolioWithSellAllQuantityOfInstrument() {
		
		System.out.println(dao.getPortfolioFromIdAndLoadOfInstrument(portfolio1.getPortfolioId(), "Q345"));
		
		// balance =10000
		Order order = new Order("UUTT789", "S", portfolio1.getClientId(), portfolio1.getPortfolioId(), "Q345", 10,
				new BigDecimal(100));
		Trade trade = new Trade("TRADE_ID-1", order.getDirection(), order, order.getClientId(), order.getPortfolioId(),
				order.getInstrumentId(), null, order.getQuantity(),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())).subtract(new BigDecimal(3)));

		// updated balance = 10000+997 =10997

		List<PortfolioHoldings> newHoldings1 = new ArrayList<>();

		int oldCountHoldingsOfInstrument = countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"
				+ portfolio1.getPortfolioId() + "' and instrument_id='" + trade.getInstrumentId() + "'");
		assertEquals(1, oldCountHoldingsOfInstrument);

		// updating
		service.updatePortfolioFromTrade(trade);

		Portfolio newPortfolio1 = new Portfolio(portfolio1.getPortfolioId(), portfolio1.getClientId(),
				portfolio1.getPortfolioTypeName(), BigDecimal.valueOf(10997), portfolio1.getPortfolioName(),
				newHoldings1);

		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "portfolio_holding", "PORTFOLIO_ID='"
				+ portfolio1.getPortfolioId() + "' and instrument_id='" + trade.getInstrumentId() + "'"));
		assertEquals(newPortfolio1,
				dao.getPortfolioFromIdAndLoadOfInstrument(portfolio1.getPortfolioId(), trade.getInstrumentId()),
				"Must get updated portfolio alter trade execution");

	}

	@Test
	public void updatePortfolioMustThrowExceptionOnUpdatingNonExistingPortfolio() {
		assertThrows(DatabaseException.class, () -> {
			Portfolio newPortfolio = new Portfolio(UUID.randomUUID().toString(), clientId, "BROKERAGE-B",
					BigDecimal.valueOf(10000), "NIKHIL Second new PORTFOLIO", null);
			dao.updatePortfolioFromIdAndLoadOfInstrument(newPortfolio, "ABC");
		});

	}

	@Test
	public void deletePortfolioFromItsId() {
		int oldPortfolioCount = countRowsInTable(jdbcTemplate, "PORTFOLIO");
		int oldHoldingsCount = countRowsInTable(jdbcTemplate, "portfolio_holding");

		service.deletePortfolioById(portfolio1.getPortfolioId());

		assertEquals(oldPortfolioCount - 1, countRowsInTable(jdbcTemplate, "PORTFOLIO"));
		assertEquals(oldHoldingsCount - 2, countRowsInTable(jdbcTemplate, "portfolio_holding"));
		assertNull(dao.getPortfolioForAuserFromPortfolioId(portfolio1.getPortfolioId()));
	}

	@Test
	public void deletePortfolioFromItsIdWithNoHoldings() {
		int oldPortfolioCount = countRowsInTable(jdbcTemplate, "PORTFOLIO");
		int oldHoldingsCount = countRowsInTable(jdbcTemplate, "portfolio_holding");

		service.deletePortfolioById(portfolio2.getPortfolioId());

		assertEquals(oldPortfolioCount - 1, countRowsInTable(jdbcTemplate, "PORTFOLIO"));
		assertEquals(oldHoldingsCount, countRowsInTable(jdbcTemplate, "portfolio_holding"));
		assertNull(dao.getPortfolioForAuserFromPortfolioId(portfolio2.getPortfolioId()));
	}

	@Test
	public void deletePortfolioNotExisting() {
		assertThrows(DatabaseException.class, () -> {
			service.deletePortfolioById(UUID.randomUUID().toString());
		});
	}

	@Test
	public void deletePortfolioOfClient() {
		int oldPortfolioCount = countRowsInTable(jdbcTemplate, "PORTFOLIO");
		int oldHoldingsCount = countRowsInTable(jdbcTemplate, "portfolio_holding");

		service.deletePortfolioByClientId(clientId);

		assertEquals(oldPortfolioCount - 2, countRowsInTable(jdbcTemplate, "PORTFOLIO"));
		assertEquals(oldHoldingsCount - 2, countRowsInTable(jdbcTemplate, "portfolio_holding"));
		assertEquals(0, dao.getPortfoliosForAUser(clientId).size());
	}

	@Test
	public void addnewUserPortfolio() {
		int oldPortfolioCount = countRowsInTable(jdbcTemplate, "PORTFOLIO");
		int oldHoldingsCount = countRowsInTable(jdbcTemplate, "portfolio_holding");

		service.addNewPortfolio(newPortfolio);

		assertEquals(oldPortfolioCount + 1, countRowsInTable(jdbcTemplate, "PORTFOLIO"));
		assertEquals(oldHoldingsCount, countRowsInTable(jdbcTemplate, "portfolio_holding"));
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "PORTFOLIO",
				"PORTFOLIO_ID='" + newPortfolio.getPortfolioId() + "'"));
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "portfolio_holding",
				"PORTFOLIO_ID='" + newPortfolio.getPortfolioId() + "'"));
		assertEquals(3, dao.getPortfoliosForAUser(clientId).size());
	}

	@Test
	public void addnewUserExistingPortfolioMustThrowError() {
		assertThrows(DuplicateKeyException.class, () -> {
			service.addNewPortfolio(portfolio1);
		});
	}

	@Test
	public void addnewUserPortfolioOfNonexistingUserMustThrowError() {
		assertThrows(NotFoundException.class, () -> {
			Portfolio newPortfolio = new Portfolio(UUID.randomUUID().toString(), BigInteger.valueOf(76876989),
					"BROKERAGE-B", BigDecimal.valueOf(10000), "NIKHIL Second new PORTFOLIO", null);
			service.addNewPortfolio(newPortfolio);
		});
	}

	@Test
	public void deletePortfolioNotExistingClient() {
		assertThrows(NotFoundException.class, () -> {
			service.deletePortfolioByClientId(BigInteger.valueOf(65869897));
		});
	}

}
