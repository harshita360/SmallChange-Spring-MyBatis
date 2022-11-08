/*
 * package com.fidelity.service.impl;
 * 
 * import static org.junit.jupiter.api.Assertions.*;
 * 
 * import java.math.BigDecimal; import java.math.BigInteger; import
 * java.time.LocalDateTime;
 * 
 * import javax.sql.DataSource;
 * 
 * import org.junit.jupiter.api.BeforeEach; import
 * org.junit.jupiter.api.DisplayName; import org.junit.jupiter.api.Test; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.test.context.SpringBootTest; import
 * org.springframework.jdbc.core.JdbcTemplate; import
 * org.springframework.test.context.jdbc.Sql; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import com.fidelity.dao.ActivityDao; import com.fidelity.models.Order; import
 * com.fidelity.models.Trade; import com.fidelity.service.ActivityService;
 * 
 * @SpringBootTest
 * 
 * @Sql(scripts={"classpath:schema.sql", "classpath:data.sql"},executionPhase =
 * Sql.ExecutionPhase.BEFORE_TEST_METHOD)
 * 
 * @Sql(scripts={"classpath:drop.sql"},executionPhase =
 * Sql.ExecutionPhase.AFTER_TEST_METHOD)
 * 
 * @Transactional class ActivityServiceMyBatisDaoIntegrationTest {
 * 
 * JdbcTemplate jdbcTemplate;
 * 
 * @Autowired ActivityService service;
 * 
 * @Autowired ActivityDao dao;
 * 
 * @Autowired DataSource dataSource;
 * 
 * private Trade trade1; private Order order1;
 * 
 * 
 * @BeforeEach void setUp() throws Exception { jdbcTemplate=new
 * JdbcTemplate(dataSource); order1 = new Order("UUUUUU1", "B", new
 * BigInteger("3463464356"), "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454", "Q3F", 10,
 * new BigDecimal("10.65")); trade1 = new
 * Trade("v8c3de3d-1fea-4d7c-a8b0-29f63c4c34bb", "B", order1, new
 * BigInteger("3463464356"), "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454", "Q3F",
 * LocalDateTime.now(), 10, new BigDecimal("106.5"), new BigDecimal("112.45"));
 * }
 * 
 * 
 * @Test void testGetUserActivitySucess() { List<Trade> trades =
 * dao.getUserActivity(new BigInteger("3463464356"));
 * 
 * assertEquals(1, trades.size()); }
 * 
 * @Test void testGetPortfolioActivitySucess() { List<Trade> trades =
 * dao.getPortfolioActivity("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454");
 * 
 * assertEquals(1, trades.size()); }
 * 
 * @Test void testInsertActivitySuccess() {
 * 
 * int oldSize = countRowsInTable(jdbcTemplate, "trade_history"); int
 * orderOldSize = countRowsInTable(jdbcTemplate, "order_data");
 * 
 * Trade newTrade = new Trade("abcdef", "B", order1, new
 * BigInteger("3463464356"), "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454", "Q3F",
 * LocalDateTime.now(), 10, new BigDecimal("106.5"), new BigDecimal("112.45"));
 * 
 * dao.addActivity(newTrade);
 * 
 * assertEquals(oldSize + 1, countRowsInTable(jdbcTemplate, "trade_history"));
 * assertEquals(orderOldSize + 1, countRowsInTable(jdbcTemplate, "order_data"));
 * 
 * }
 * 
 * 
 * @Test void testInsertActivity_Duplicate_ThrowsException() {
 * assertThrows(DuplicateKeyException.class, () -> { dao.addActivity(trade1);
 * }); }
 * 
 * 
 * @Test void testDeleteActivityClientId_Success() { BigInteger id = new
 * BigInteger("3463464356"); int oldSize = countRowsInTable(jdbcTemplate,
 * "trade_history"); int orderOldSize = countRowsInTable(jdbcTemplate,
 * "order_data"); assertEquals(1, countRowsInTableWhere(jdbcTemplate,
 * "trade_history", "client_id = " + id)); assertEquals(1,
 * countRowsInTableWhere(jdbcTemplate, "order_data", "client_id = " + id));
 * 
 * dao.deleteActivityClientId(id);
 * 
 * assertEquals(oldSize - 1, countRowsInTable(jdbcTemplate, "trade_history"));
 * assertEquals(orderOldSize - 1, countRowsInTable(jdbcTemplate, "order_data"));
 * assertEquals(0, countRowsInTableWhere(jdbcTemplate, "trade_history",
 * "client_id = " + id)); assertEquals(0, countRowsInTableWhere(jdbcTemplate,
 * "order_data", "client_id = " + id)); }
 * 
 * @Test void testDeleteActivityClientId_NotPresent_Success() { BigInteger id =
 * new BigInteger("3463464566"); assertEquals(0,
 * countRowsInTableWhere(jdbcTemplate, "trade_history", "client_id = " + id));
 * assertEquals(0, countRowsInTableWhere(jdbcTemplate, "order_data",
 * "client_id = " + id));
 * 
 * assertThrows(DatabaseException.class,()->{ dao.deleteActivityClientId(id);
 * });
 * 
 * 
 * }
 * 
 * @Test void testDeleteActivityPortfolioId_Success() { String id =
 * "f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"; int oldSize =
 * countRowsInTable(jdbcTemplate, "trade_history"); int orderOldSize =
 * countRowsInTable(jdbcTemplate, "order_data"); assertEquals(1,
 * countRowsInTableWhere(jdbcTemplate, "trade_history", "portfolio_id = '" + id
 * + "'")); assertEquals(1, countRowsInTableWhere(jdbcTemplate, "order_data",
 * "portfolio_id = '" + id + "'"));
 * 
 * dao.deleteActivityPortfolioId(id);
 * 
 * assertEquals(oldSize - 1, countRowsInTable(jdbcTemplate, "trade_history"));
 * assertEquals(orderOldSize - 1, countRowsInTable(jdbcTemplate, "order_data"));
 * assertEquals(0, countRowsInTableWhere(jdbcTemplate, "trade_history",
 * "portfolio_id = '" + id + "'")); assertEquals(0,
 * countRowsInTableWhere(jdbcTemplate, "order_data", "portfolio_id = '" + id +
 * "'"));
 * 
 * }
 * 
 * @Test void testDeleteActivityPortfolioId_NotPresent_Success() { String id =
 * "f8c3de3d-1fea-4d7c-a8b0-29f4c3454"; assertEquals(0,
 * countRowsInTableWhere(jdbcTemplate, "trade_history", "portfolio_id = '" + id
 * + "'")); assertEquals(0, countRowsInTableWhere(jdbcTemplate, "order_data",
 * "portfolio_id = '" + id + "'"));
 * 
 * assertThrows(DatabaseException.class,()->{ dao.deleteActivityPortfolioId(id);
 * });
 * 
 * 
 * }
 * 
 * }
 */