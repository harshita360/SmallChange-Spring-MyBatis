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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.fidelity.models.Order;
import com.fidelity.models.Trade;
import com.fidelity.utils.AuthenticationData;
import com.fidelity.utils.TokenDto;

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
	HttpHeaders header;
	HttpHeaders header2;
	
	@BeforeEach
	void setUp() {
		clientId=BigInteger.valueOf(970531476);
		portfolioId="f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454";
		Order order = new Order("a8c3de3d-1fea-4d7c-a8b0-29f63c4c3455", "B", clientId, portfolioId, "Q34F", 10,
				new BigDecimal(10.65).setScale(2, RoundingMode.HALF_EVEN));
		trade = new Trade("v8c3de3d-1fea-4d7c-a8b0-29f63c4c34bb", order.getDirection(), order, order.getClientId(), order.getPortfolioId(),
				order.getInstrumentId(), LocalDateTime.of(1999, 9, 30, 01, 02,59), order.getQuantity(),
				new BigDecimal(106.5).setScale(1, RoundingMode.HALF_EVEN),
				new BigDecimal(112.45).setScale(2, RoundingMode.HALF_EVEN));
		activity=new ArrayList<>();
		activity.add(trade);
		

		header = new HttpHeaders();
		AuthenticationData authData = new AuthenticationData("nikhil@gmail.com", "NIKHIL@123");
		ResponseEntity<TokenDto> token = restTemplate.postForEntity("/clients/login", authData, TokenDto.class);
		header.add("Authorization", "Bearer " + token.getBody().getToken());

		header2 = new HttpHeaders();
		AuthenticationData authData2 = new AuthenticationData("nik@gmail.com", "NIKHIL@123");
		ResponseEntity<TokenDto> token2 = restTemplate.postForEntity("/clients/login", authData2, TokenDto.class);
		header2.add("Authorization", "Bearer " + token2.getBody().getToken());
	}
	
	
	@AfterEach
	public void teadrDown() {
		dropAlltables();
	}
	@Test
	void getUserActivity() throws Exception {
		
		String requestUrl="/activity/client";
		
		HttpEntity<Object> entity = new HttpEntity<>(header);
		
		ResponseEntity<Trade[]> response=restTemplate.exchange(requestUrl,HttpMethod.GET,entity,Trade[].class);
		
		assertEquals(HttpStatus.OK,response.getStatusCode());
		
		Trade[] retrived=response.getBody();
		assertArrayEquals(activity.toArray(), retrived);
	}
	
	@Test
	void getUserActivity_Auth_Error() throws Exception{
		
		String requestUrl = "/activity/client";
		ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}
	
	@Test
	void getUserActivity_Not_Found() throws Exception {

		String requestUrl="/activity/client";
		
		HttpEntity<Object> entity = new HttpEntity<>(header);
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "client", "client_id='"+clientId.toString()+"'");
		
		ResponseEntity<Trade[]> response=restTemplate.exchange(requestUrl,HttpMethod.GET,entity,Trade[].class);
		
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

	}
	
	@Test
	void getUserActivity_Server_Error() throws Exception {
		String requestUrl="/activity/client";
		
		HttpEntity<Object> entity = new HttpEntity<>(header);
		
		JdbcTestUtils.dropTables(jdbcTemplate, "TRADE_HISTORY");
		JdbcTestUtils.dropTables(jdbcTemplate, "ORDER_DATA");
		
		ResponseEntity<String> response=restTemplate.exchange(requestUrl,HttpMethod.GET,entity,String.class);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	
	}
	
	@Test
	void getUserActivity_Empty_List() throws Exception {
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "trade_history", "client_id='"+clientId.toString()+"'");
		
		
		String requestUrl="/activity/client";
		
		HttpEntity<Object> entity = new HttpEntity<>(header);
		
		ResponseEntity<Trade[]> response=restTemplate.exchange(requestUrl,HttpMethod.GET,entity,Trade[].class);
		
		assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());

	
	}
	
	@Test
	void getPortfolioActivity() throws Exception {
		
		String requestUrl="/activity/{portfolioId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolioId);
		HttpEntity<Object> entity = new HttpEntity<>(header);
		
		ResponseEntity<Trade[]> response=restTemplate.exchange(requestUrl,HttpMethod.GET,entity, Trade[].class, params);
		
		assertEquals(HttpStatus.OK,response.getStatusCode());
		
		Trade[] retrived=response.getBody();
		assertArrayEquals(activity.toArray(), retrived);
	}
	
	@Test
	void getPortfolioActivity_Access_Denied() throws Exception {
		
		String requestUrl="/activity/{portfolioId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolioId);
		HttpEntity<Object> entity = new HttpEntity<>(header2);
		
		ResponseEntity<String> response=restTemplate.exchange(requestUrl,HttpMethod.GET,entity, String.class, params);
		
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}
	
	@Test
	void getPortfolioActivity_Auth_Error() throws Exception {
		
		String requestUrl="/activity/{portfolioId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolioId);
		
		ResponseEntity<String> response=restTemplate.exchange(requestUrl,HttpMethod.GET,null, String.class, params);
		
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}
	
	@Test
	void getPortfolioActivity_Not_Found() throws Exception {
		String requestUrl="/activity/{portfolioId}";
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolioId);
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "client", "client_id='"+clientId.toString()+"'");
		
		HttpEntity<Object> entity = new HttpEntity<>(header);
		ResponseEntity<Trade[]> response=restTemplate.exchange(requestUrl,HttpMethod.GET,entity, Trade[].class, params);
		
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

	}
	
	@Test
	void getPortfolioActivity_Server_Error() throws Exception {
		String requestUrl="/activity/{portfolioId}";
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolioId);
		
		JdbcTestUtils.dropTables(jdbcTemplate, "TRADE_HISTORY");
		JdbcTestUtils.dropTables(jdbcTemplate, "ORDER_DATA");
		
		HttpEntity<Object> entity = new HttpEntity<>(header);
		
		ResponseEntity<String> response=restTemplate.exchange(requestUrl,HttpMethod.GET,entity, String.class, params);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	
	}
	
	@Test
	void getPortfolioActivity_Empty_List() throws Exception {
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "trade_history", "client_id='"+clientId.toString()+"'");
		
		
		String requestUrl="/activity/{portfolioId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolioId);
		
		HttpEntity<Object> entity = new HttpEntity<>(header);
		
		ResponseEntity<Trade[]> response=restTemplate.exchange(requestUrl,HttpMethod.GET,entity, Trade[].class, params);
		
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
