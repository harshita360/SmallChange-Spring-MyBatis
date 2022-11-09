package com.fidelity.dao.impl.myBatis;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.dao.ActivityDao;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.models.Order;
import com.fidelity.models.Trade;

@SpringBootTest
@Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = { "classpath:drop.sql", }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

@Transactional
class ActivityDaoMyBatisImplTest {

	@Autowired
	DataSource dataSource;

	JdbcTemplate jdbcTemplate;

	@Autowired
	ActivityDao dao;
	Connection connection;

	private Trade trade1;
	private Order order1;
	private BigInteger clientId;
	private String portfolioId;

	@BeforeEach
	void setUp() throws Exception {
		jdbcTemplate = new JdbcTemplate(dataSource);
		clientId = new BigInteger("970531476");
		portfolioId = "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454";
		order1 = new Order("a8c3de3d-1fea-4d7c-a8b0-29f63c4c3455", "B", clientId, portfolioId, "Q3F", 10,
				new BigDecimal("10.65"));
		trade1 = new Trade("v8c3de3d-1fea-4d7c-a8b0-29f63c4c34bb", "B", order1, clientId,
				"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454", "Q3F", LocalDateTime.now(), 10, new BigDecimal("106.5"),
				new BigDecimal("112.45"));
	}

	@Test
	void testGetUserActivitySucess() {
		List<Trade> trades = dao.getUserActivity(clientId);

		assertEquals(1, trades.size());
	}

	@Test
	void testGetPortfolioActivitySucess() {
		List<Trade> trades = dao.getPortfolioActivity(portfolioId);

		assertEquals(1, trades.size());
	}

	@Test
	void testInsertActivitySuccess() {

		int oldSize = countRowsInTable(jdbcTemplate, "trade_history");

		Order newOrder = new Order("UUUUU1", "B", clientId, portfolioId, "Q3F", 10, new BigDecimal("10.65"));

		Trade newTrade = new Trade("abcdef", "B", newOrder, clientId, portfolioId, "Q3F", LocalDateTime.now(), 10,
				new BigDecimal("106.5"), new BigDecimal("112.45"));

		dao.addOrder(newOrder);
		dao.addActivity(newTrade);

		assertEquals(oldSize + 1, countRowsInTable(jdbcTemplate, "trade_history"));

	}

	@Test
	void testInsertActivity_Duplicate_ThrowsException() {
		assertThrows(DuplicateKeyException.class, () -> {
			dao.addActivity(trade1);
		});
	}

	@Test
	void testInsertOrderSuccess() {

		int orderOldSize = countRowsInTable(jdbcTemplate, "order_data");

		Order newOrder = new Order("UUUUU1", "B", clientId, portfolioId, "Q3F", 10, new BigDecimal("10.65"));

		dao.addOrder(newOrder);

		assertEquals(orderOldSize + 1, countRowsInTable(jdbcTemplate, "order_data"));

	}

	@Test
	void testInsertOrder_Duplicate_ThrowsException() {
		assertThrows(DuplicateKeyException.class, () -> {
			dao.addOrder(order1);
		});
	}

	@Test
	void testDeleteActivityClientId_Success() {
		int oldSize = countRowsInTable(jdbcTemplate, "trade_history");
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "trade_history", "client_id = " + clientId));

		dao.deleteActivityClientId(clientId);

		assertEquals(oldSize - 1, countRowsInTable(jdbcTemplate, "trade_history"));
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "trade_history", "client_id = " + clientId));
	}

	@Test
	void testDeleteActivityClientId_NotPresent_Success() {
		BigInteger id = new BigInteger("3463464566");
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "trade_history", "client_id = " + id));

		assertThrows(DatabaseException.class, () -> {
			dao.deleteActivityClientId(id);
		});

	}

	@Test
	void testDeleteActivityPortfolioId_Success() {
		int oldSize = countRowsInTable(jdbcTemplate, "trade_history");
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "trade_history", "portfolio_id = '" + portfolioId + "'"));

		dao.deleteActivityPortfolioId(portfolioId);

		assertEquals(oldSize - 1, countRowsInTable(jdbcTemplate, "trade_history"));
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "trade_history", "portfolio_id = '" + portfolioId + "'"));

	}

	@Test
	void testDeleteActivityPortfolioId_NotPresent_Success() {
		String id = "f8c3de3d-1fea-4d7c-a8b0-29f4c3454";
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "trade_history", "portfolio_id = '" + id + "'"));

		assertThrows(DatabaseException.class, () -> {
			dao.deleteActivityPortfolioId(id);
		});

	}

	@Test
	void testDeleteOrderClientId_Success() {
		int orderOldSize = countRowsInTable(jdbcTemplate, "order_data");
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "order_data", "client_id = " + clientId));

		dao.deleteOrderClientId(clientId);

		assertEquals(orderOldSize - 1, countRowsInTable(jdbcTemplate, "order_data"));
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "order_data", "client_id = " + clientId));
	}

	@Test
	void testDeleteOrderClientId_NotPresent_Success() {
		BigInteger id = new BigInteger("3463464566");
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "order_data", "client_id = " + id));

		assertThrows(DatabaseException.class, () -> {
			dao.deleteOrderClientId(id);
		});

	}

	@Test
	void testDeleteOrderPortfolioId_Success() {
		int orderOldSize = countRowsInTable(jdbcTemplate, "order_data");
		assertEquals(1, countRowsInTableWhere(jdbcTemplate, "order_data", "portfolio_id = '" + portfolioId + "'"));

		dao.deleteOrderPortfolioId(portfolioId);

		assertEquals(orderOldSize - 1, countRowsInTable(jdbcTemplate, "order_data"));
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "order_data", "portfolio_id = '" + portfolioId + "'"));

	}

	@Test
	void testDeleteOrderPortfolioId_NotPresent_Success() {
		String id = "f8c3de3d-1fea-4d7c-a8b0-29f4c3454";
		assertEquals(0, countRowsInTableWhere(jdbcTemplate, "order_data", "portfolio_id = '" + id + "'"));

		assertThrows(DatabaseException.class, () -> {
			dao.deleteOrderPortfolioId(id);
		});

	}

}
