package com.fidelity.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.exceptions.NotFoundException;
import com.fidelity.models.Order;
import com.fidelity.models.Trade;
import com.fidelity.service.ActivityService;

@SpringBootTest
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = { "classpath:drop.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Transactional
class ActivityServiceMyBatisDaoIntegrationTest {

	@Autowired
	@Qualifier("proxyActivityService")
	ActivityService service;

	JdbcTemplate jdbcTemplate;

	@Autowired
	DataSource dataSource;

	private Trade trade1;
	private Order order1;
	private BigInteger clientId;
	private String portfolioId;

	@BeforeEach
	void setUp() throws Exception {
		jdbcTemplate = new JdbcTemplate(dataSource);

		clientId = BigInteger.valueOf(970531476);
		portfolioId = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454";
		order1 = new Order("a8c3de3d-1fea-4d7c-a8b0-29f63c4c3455", "B", clientId, portfolioId, "Q34F", 10,
				new BigDecimal(10.65).setScale(2, RoundingMode.HALF_EVEN));
		trade1 = new Trade("v8c3de3d-1fea-4d7c-a8b0-29f63c4c34bb", order1.getDirection(), order1, order1.getClientId(),
				order1.getPortfolioId(), order1.getInstrumentId(), LocalDateTime.of(1999, 9, 30, 01, 02, 59),
				order1.getQuantity(), new BigDecimal(106.5).setScale(1, RoundingMode.HALF_EVEN),
				new BigDecimal(112.45).setScale(2, RoundingMode.HALF_EVEN));

	}

	@Test
	void getUserActivitySuccess() {
		List<Trade> trades = service.getUserActivity(clientId);
		assertEquals(trades.size(), countRowsInTableWhere(jdbcTemplate, "trade_history", "client_id=" + clientId));
		assertTrue(trades.contains(trade1));
	}

	@Test
	void getUserActivityFailure_EmptyActivity() {
		BigInteger id = new BigInteger("123456");
		assertThrows(NotFoundException.class, () -> {
			service.getUserActivity(id);
		});
	}

	@Test
	void getPortfolioActivitySuccess() {
		List<Trade> trades = service.getPortfolioActivity(portfolioId);
		assertEquals(trades.size(),
				countRowsInTableWhere(jdbcTemplate, "trade_history", "portfolio_id='" + portfolioId + "'"));
		assertTrue(trades.contains(trade1));
	}

	@Test
	void getPortfolioActivityFailure_EmptyActivity() {
		String id = "UUUUU1";
		assertThrows(NotFoundException.class, () -> {
			service.getPortfolioActivity(id);
		});

	}

	@Test
	void deleteActivityClientId_Success() {
		int oldSize = countRowsInTable(jdbcTemplate, "trade_history");
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "trade_history", "client_id = " + clientId));

		service.deleteActivityClientId(clientId);

		assertEquals(oldSize - 1, countRowsInTable(jdbcTemplate, "trade_history"));
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "trade_history", "client_id = " + clientId));
	}

	@Test
	void testDeleteActivityClientId_NotPresent_Success() {
		BigInteger id = new BigInteger("3463464566");
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "trade_history", "client_id = " + id));

		assertThrows(NotFoundException.class, () -> {
			service.deleteActivityClientId(id);
		});

	}

	@Test
	void testDeleteActivityPortfolioId_Success() {
		int oldSize = countRowsInTable(jdbcTemplate, "trade_history");
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "trade_history", "portfolio_id = '" + portfolioId + "'"));

		service.deleteActivityPortfolioId(portfolioId);

		assertEquals(oldSize - 1, countRowsInTable(jdbcTemplate, "trade_history"));
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "trade_history", "portfolio_id = '" + portfolioId + "'"));

	}

	@Test
	void testDeleteActivityPortfolioId_NotPresent_Success() {
		String id = "f8c3de3d-1fea-4d7c-a8b0-29f4c3454";
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "trade_history", "portfolio_id = '" + id + "'"));

		assertThrows(NotFoundException.class, () -> {
			service.deleteActivityPortfolioId(id);
		});

	}

	@Test
	void testDeleteOrderClientId_Success() {
		int orderOldSize = countRowsInTable(jdbcTemplate, "order_data");
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "order_data", "client_id = " + clientId));

		service.deleteOrderClientId(clientId);

		assertEquals(orderOldSize - 1, countRowsInTable(jdbcTemplate, "order_data"));
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "order_data", "client_id = " + clientId));
	}

	@Test
	void testDeleteOrderClientId_NotPresent_Success() {
		BigInteger id = new BigInteger("3463464566");
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "order_data", "client_id = " + id));

		assertThrows(NotFoundException.class, () -> {
			service.deleteOrderClientId(id);
		});

	}

	@Test
	void testDeleteOrderPortfolioId_Success() {
		int orderOldSize = countRowsInTable(jdbcTemplate, "order_data");
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "order_data", "portfolio_id = '" + portfolioId + "'"));

		service.deleteOrderPortfolioId(portfolioId);

		assertEquals(orderOldSize - 1, countRowsInTable(jdbcTemplate, "order_data"));
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "order_data", "portfolio_id = '" + portfolioId + "'"));

	}

	@Test
	void testDeleteOrderPortfolioId_NotPresent_Success() {
		String id = "f8c3de3d-1fea-4d7c-a8b0-29f4c3454";
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "order_data", "portfolio_id = '" + id + "'"));

		assertThrows(NotFoundException.class, () -> {
			service.deleteOrderPortfolioId(id);
		});

	}

	@Test
	void testInsertActivitySuccess() {

		int oldSize = countRowsInTable(jdbcTemplate, "trade_history");

		Order newOrder = new Order("UUUUU1", "B", clientId, portfolioId, "Q3F", 10, new BigDecimal("10.65"));

		Trade newTrade = new Trade("abcdef", "B", newOrder, clientId, portfolioId, "Q3F", LocalDateTime.now(), 10,
				new BigDecimal("106.5"), new BigDecimal("112.45"));

		service.addOrder(newOrder);
		service.addActivity(newTrade);

		assertEquals(oldSize + 1, countRowsInTable(jdbcTemplate, "trade_history"));

	}

	@Test
	void testInsertActivity_Duplicate_ThrowsException() {
		assertThrows(DuplicateKeyException.class, () -> {
			service.addActivity(trade1);
		});
	}

	@Test
	void testInsertOrderSuccess() {

		int orderOldSize = countRowsInTable(jdbcTemplate, "order_data");

		Order newOrder = new Order("UUUUU1", "B", clientId, portfolioId, "Q3F", 10, new BigDecimal("10.65"));

		service.addOrder(newOrder);

		assertEquals(orderOldSize + 1, countRowsInTable(jdbcTemplate, "order_data"));

	}

//	@Test
//	void testInsertOrder_Duplicate_ThrowsException() {
//		assertThrows(DuplicateKeyException.class, () -> {
//			service.addOrder(order1);
//		});
//	}

}
