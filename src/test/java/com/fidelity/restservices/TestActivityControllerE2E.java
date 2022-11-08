package com.fidelity.restservices;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.fidelity.models.Order;
import com.fidelity.models.Portfolio;
import com.fidelity.models.Trade;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Sql(scripts={"classpath:schema.sql", "classpath:data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)  
class TestActivityControllerE2E {

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	BigInteger clientId;
	Order order;
	Trade trade;
	String portfolioId;
	List<Trade> activity;
	
	@BeforeEach
	void setUp() {
		clientId=BigInteger.valueOf(346346435);
		portfolioId="f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454";
		Order order = new Order("a8c3de3d-1fea-4d7c-a8b0-29f63c4c3455", "B", clientId, portfolioId, "Q34F", 10,
				new BigDecimal(10.65).setScale(2, RoundingMode.HALF_EVEN));
		trade = new Trade("v8c3de3d-1fea-4d7c-a8b0-29f63c4c34bb", order.getDirection(), order, order.getClientId(), order.getPortfolioId(),
				order.getInstrumentId(), LocalDateTime.of(1999, 9, 30, 01, 02,59), order.getQuantity(),
				new BigDecimal(106.5).setScale(1, RoundingMode.HALF_EVEN),
				new BigDecimal(112.45).setScale(2, RoundingMode.HALF_EVEN));
		activity=new ArrayList<>();
		activity.add(trade);
	}
	
	
	@AfterEach
	public void teadrDown() {
		dropAlltables();
	}
	@Test
	void getUserActivity() throws Exception {
		
		String requestUrl="/activity/client/{clientId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		ResponseEntity<Trade[]> response=restTemplate.getForEntity(requestUrl, Trade[].class, params);
		
		assertEquals(HttpStatus.OK,response.getStatusCode());
		
		Trade[] retrived=response.getBody();
		assertArrayEquals(activity.toArray(), retrived);
	}
	
	@Test
	void getUserActivity_Not_Found() throws Exception {
		String requestUrl="/activity/client/{clientId}";
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "client", "client_id='"+clientId.toString()+"'");
		
		ResponseEntity<Trade[]> response=restTemplate.getForEntity(requestUrl, Trade[].class, params);
		
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

	}
	
	@Test
	void getUserActivity_Server_Error() throws Exception {
		String requestUrl="/activity/client/{clientId}";
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		JdbcTestUtils.dropTables(jdbcTemplate, "TRADE_HISTORY");
		JdbcTestUtils.dropTables(jdbcTemplate, "ORDER_DATA");
		
		ResponseEntity<String> response=restTemplate.getForEntity(requestUrl, String.class, params);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	
	}
	
	@Test
	void getUserActivity_Empty_List() throws Exception {
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "trade_history", "client_id='"+clientId.toString()+"'");
		
		
		String requestUrl="/activity/client/{clientId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		ResponseEntity<Portfolio[]> response=restTemplate.getForEntity(requestUrl, Portfolio[].class, params);
		
		assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());

	
	}
	
	@Test
	void getPortfolioActivity() throws Exception {
		
		String requestUrl="/activity/{portfolioId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolioId);
		
		ResponseEntity<Trade[]> response=restTemplate.getForEntity(requestUrl, Trade[].class, params);
		
		assertEquals(HttpStatus.OK,response.getStatusCode());
		
		Trade[] retrived=response.getBody();
		assertArrayEquals(activity.toArray(), retrived);
	}
	
	@Test
	void getPortfolioActivity_Not_Found() throws Exception {
		String requestUrl="/activity/{portfolioId}";
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolioId);
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "client", "client_id='"+clientId.toString()+"'");
		
		ResponseEntity<Trade[]> response=restTemplate.getForEntity(requestUrl, Trade[].class, params);
		
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

	}
	
	@Test
	void getPortfolioActivity_Server_Error() throws Exception {
		String requestUrl="/activity/{portfolioId}";
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolioId);
		
		JdbcTestUtils.dropTables(jdbcTemplate, "TRADE_HISTORY");
		JdbcTestUtils.dropTables(jdbcTemplate, "ORDER_DATA");
		
		ResponseEntity<String> response=restTemplate.getForEntity(requestUrl, String.class, params);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	
	}
	
	@Test
	void getPortfolioActivity_Empty_List() throws Exception {
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "trade_history", "client_id='"+clientId.toString()+"'");
		
		
		String requestUrl="/activity/{portfolioId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolioId);
		
		ResponseEntity<Portfolio[]> response=restTemplate.getForEntity(requestUrl, Portfolio[].class, params);
		
		assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());

	
	}
	
	private void dropAlltables() {
		String[] tables=new String[] {"TRADE_HISTORY","ORDER_DATA","PORTFOLIO_HOLDING","PORTFOLIO","INVESTMENT_PREFERENCE","CLIENT"};
		for(String table:tables) {
			this.dropTablesIfExists(table);
		}
	}
	
	
	private void dropTablesIfExists(String tableName) {
		try {
			JdbcTestUtils.dropTables(jdbcTemplate, tableName);
		}catch(Exception e) {
			
		}
	}

}
