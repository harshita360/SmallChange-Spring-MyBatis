package com.fidelity.restservices;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;
import com.fidelity.utils.PortfolioUtils;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@Sql(scripts={"classpath:schema.sql", "classpath:data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)  
public class TestPortfolioControllerE2E {

	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	Portfolio portfolio1;
	Portfolio portfolio2;
	List<Portfolio> portfolios;
	BigInteger clientId;
	
	@BeforeEach
	public void setUp() {
		
		
		
		clientId=BigInteger.valueOf(346346435);
		portfolio1=new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3121",clientId,
				"BROKERAGE-B",BigDecimal.valueOf(10000),"NIKHIL Second PORTFOLIO",
				new ArrayList<>()
				);
		List<PortfolioHoldings> holdings=new ArrayList<>();
		holdings.add(new PortfolioHoldings("Q347", BigInteger.valueOf(10), BigDecimal.valueOf(876.97), LocalDateTime.of(1999, 9, 29, 23, 29,29), LocalDateTime.of(1999, 9, 29, 23, 29,29)));
		holdings.add(new PortfolioHoldings("Q345", BigInteger.valueOf(10), BigDecimal.valueOf(876.97), LocalDateTime.of(1999, 9, 29, 23, 59,59), LocalDateTime.of(1999, 9, 29, 23, 59,59)));
		portfolio2=new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454",clientId,
				"BROKERAGE",BigDecimal.valueOf(10000),"NIKHIL FIRST PORTFOLIO",
				holdings
				);
		
		portfolios=new ArrayList<>();
		portfolios.add(portfolio1);
		portfolios.add(portfolio2);
		
	}
	
	@AfterEach
	public void teadrDown() {
		dropAlltables();
	}
	@Test
	void getAllUserPortfolios() throws Exception {
		
		String requestUrl="/portfolios/client/{clientId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		ResponseEntity<Portfolio[]> response=restTemplate.getForEntity(requestUrl, Portfolio[].class, params);
		
		assertEquals(response.getStatusCode(),HttpStatus.OK);
		
		Portfolio[] retrived=response.getBody();
		assertArrayEquals(portfolios.toArray(), retrived);
	}
	
	@Test
	public void getAllUserPortfolios_Not_Found() throws Exception {
		String requestUrl="/portfolios/client/{clientId}";
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "client", "client_id='"+clientId.toString()+"'");
		
		ResponseEntity<Portfolio[]> response=restTemplate.getForEntity(requestUrl, Portfolio[].class, params);
		
		assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);

		
	
	}
//	
//	@Test
//	public void getAllUserPortfolios_Bad_Request() throws Exception {
//		String requestUrl="/portfolios/client/{clientId}";
//		
//		Map<String,Object> params=new HashMap<>();
//		params.put("clientId", clientId);
//		
//		JdbcTestUtils.dropTables(jdbcTemplate, "client");
//		
//		ResponseEntity<Portfolio[]> response=restTemplate.getForEntity(requestUrl, Portfolio[].class, params);
//		
//		assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
//	
//	}
//	
	@Test
	public void getAllUserPortfolios_Server_Error() throws Exception {
		String requestUrl="/portfolios/client/{clientId}";
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		JdbcTestUtils.dropTables(jdbcTemplate, "TRADE_HISTORY");
		JdbcTestUtils.dropTables(jdbcTemplate, "ORDER_DATA");
		JdbcTestUtils.dropTables(jdbcTemplate, "PORTFOLIO_HOLDING");
		JdbcTestUtils.dropTables(jdbcTemplate, "PORTFOLIO");
		
		ResponseEntity<String> response=restTemplate.getForEntity(requestUrl, String.class, params);
		
		assertEquals(response.getStatusCode(),HttpStatus.INTERNAL_SERVER_ERROR);
	
	}
//	
	@Test
	public void getAllUserPortfolios_Empty_List() throws Exception {
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "PORTFOLIO", "client_id='"+clientId.toString()+"'");
		
		
		String requestUrl="/portfolios/client/{clientId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		ResponseEntity<Portfolio[]> response=restTemplate.getForEntity(requestUrl, Portfolio[].class, params);
		
		assertEquals(response.getStatusCode(),HttpStatus.NO_CONTENT);

	
	}
//	
	@Test
	public void getThePortfolioFromItsId() throws Exception {
		String requestUrl="/portfolios/{portfolioId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolio2.getPortfolioId());
		
		
		ResponseEntity<Portfolio> response=restTemplate.getForEntity(requestUrl, Portfolio.class, params);
		
		assertEquals(response.getStatusCode(),HttpStatus.OK);
		
		Portfolio retrived=response.getBody();
		assertEquals(portfolio2, retrived);
	
	}
//	
	@Test
	public void getThePortfolioFromItsId_Not_Found() throws Exception {
		String requestUrl="/portfolios/{portfolioId}";
		
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "PORTFOLIO");
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolio2.getPortfolioId());
		
		
		ResponseEntity<String> response=restTemplate.getForEntity(requestUrl, String.class, params);
		
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
		
	
	}
//	
//	@Test
//	public void getThePortfolioFromItsId_Bad_Request() throws Exception {
//		String requestUrl="/portfolios/{portfolioId}";
//		
//		when(service.getPortfolioForAuserFromPortfolioId(portfolio2.getPortfolioId())).thenThrow(DatabaseException.class);
//		
//		mockMvc.perform(get(requestUrl,portfolio2.getPortfolioId()))
//		.andDo(print())
//		.andExpect(status().isBadRequest());
//	
//	}
//	
	@Test
	void getThePortfolioFromItsId_Server_Error() throws Exception {
		String requestUrl="/portfolios/{portfolioId}";
		
		
		JdbcTestUtils.dropTables(jdbcTemplate, "TRADE_HISTORY");
		JdbcTestUtils.dropTables(jdbcTemplate, "ORDER_DATA");
		JdbcTestUtils.dropTables(jdbcTemplate, "PORTFOLIO_HOLDING");
		JdbcTestUtils.dropTables(jdbcTemplate, "PORTFOLIO");
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolio2.getPortfolioId());
		
		
		ResponseEntity<String> response=restTemplate.getForEntity(requestUrl, String.class, params);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	
	}
//	
	@Test
	void createDefaultPortfolioOfClient() throws Exception {
		
		String requestUrl="/portfolios/client/{clientId}/default";
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		
		ResponseEntity<Portfolio> response=restTemplate.postForEntity(requestUrl,null, Portfolio.class, params);
		
		assertEquals(HttpStatus.CREATED,response.getStatusCode());
		
		
		
		
		Portfolio retrived=response.getBody();
		portfolioData.setPortfolioId(retrived.getPortfolioId());
		assertEquals(portfolioData, retrived);
	}
//	
	@Test
	void createDefaultPortfolioOfClient_Not_Found() throws Exception {
		
		String requestUrl="/portfolios/client/{clientId}/default";
		
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "client", "client_id='"+clientId.toString()+"'");
		
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		
		ResponseEntity<Portfolio> response=restTemplate.postForEntity(requestUrl,null, Portfolio.class, params);
		
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
		
	}
//	
	@Test
	void createDefaultPortfolioOfClient_Server_Error() throws Exception {
		
		String requestUrl="/portfolios/client/{clientId}/default";
		
		
		JdbcTestUtils.dropTables(jdbcTemplate, "TRADE_HISTORY");
		JdbcTestUtils.dropTables(jdbcTemplate, "ORDER_DATA");
		JdbcTestUtils.dropTables(jdbcTemplate, "PORTFOLIO_HOLDING");
		JdbcTestUtils.dropTables(jdbcTemplate, "PORTFOLIO");
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		
		ResponseEntity<Portfolio> response=restTemplate.postForEntity(requestUrl,null, Portfolio.class, params);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	}
//	
//	@Test
//	void createDefaultPortfolioOfClient_Database_Error() throws Exception {
//		
//		String requestUrl="/portfolios/client/{clientId}/default";
//		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
//		
//		when(service.addNewPortfolio(portfolioData)).thenThrow(DatabaseException.class);		
//		mockMvc.perform(post(requestUrl,clientId))
//		.andDo(print())
//		.andExpect(status().isBadRequest());
//	}
//	
//	@Test
//	void createDefaultPortfolioOfClient_NotCorrectDataSupplied() throws Exception {
//		
//		String requestUrl="/portfolios/client/{clientId}/default";
//		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
//		
//		when(service.addNewPortfolio(portfolioData)).thenThrow(NotEligibleException.class);		
//		mockMvc.perform(post(requestUrl,clientId))
//		.andDo(print())
//		.andExpect(status().isBadRequest());
//	}
//	
	@Test
	void createNewPortfolioOfClient() throws Exception {
		
		String requestUrl="/portfolios/client/{clientId}";
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		
		
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		
		ResponseEntity<Portfolio> response=restTemplate.postForEntity(requestUrl,portfolioData, Portfolio.class, params);
		
		assertEquals(HttpStatus.CREATED,response.getStatusCode());
		
		Portfolio retrived=response.getBody();
		portfolioData.setPortfolioId(retrived.getPortfolioId());
		assertEquals(portfolioData, retrived);
	}
//	
	@Test
	void createNewPortfolioOfClient_Not_Found() throws Exception {
		
		String requestUrl="/portfolios/client/{clientId}";
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		
		
		JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "client", "client_id='"+clientId.toString()+"'");
		
		
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		
		ResponseEntity<String> response=restTemplate.postForEntity(requestUrl,portfolioData, String.class, params);
		
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
	}
//	
	@Test
	void createNewPortfolioOfClient_Server_Error() throws Exception {
		
		String requestUrl="/portfolios/client/{clientId}";
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		
		JdbcTestUtils.dropTables(jdbcTemplate, "TRADE_HISTORY");
		JdbcTestUtils.dropTables(jdbcTemplate, "ORDER_DATA");
		JdbcTestUtils.dropTables(jdbcTemplate, "PORTFOLIO_HOLDING");
		JdbcTestUtils.dropTables(jdbcTemplate, "PORTFOLIO");
		
		
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		
		ResponseEntity<String> response=restTemplate.postForEntity(requestUrl,portfolioData, String.class, params);
		
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
	}
//	
//	@Test
//	void createNewPortfolioOfClient_Database_Error() throws Exception {
//		
//		String requestUrl="/portfolios/client/{clientId}";
//		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
//		
//		when(service.addNewPortfolio(portfolioData)).thenThrow(DatabaseException.class);		
//		
//		ObjectMapper mapper=new ObjectMapper();
//		
//		mockMvc.perform(post(requestUrl,clientId)
//				.contentType(MediaType.APPLICATION_JSON_VALUE)
//				.content(mapper.writeValueAsString(portfolioData))
//				)
//		.andDo(print())
//		.andExpect(status().isBadRequest());
//	}
//	
	@Test
	void createNewPortfolioOfClient_no_portfolio_name_DataSupplied() throws Exception {
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		portfolioData.setPortfolioName(null);
		
		this.asertTheBadRequetOfTheCreatePortfolioOfData(portfolioData);
	}
	
	@Test
	void createNewPortfolioOfClient_empty_portfolio_name_DataSupplied() throws Exception {
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		portfolioData.setPortfolioName("");
		
		this.asertTheBadRequetOfTheCreatePortfolioOfData(portfolioData);
	}
	
	@Test
	void createNewPortfolioOfClient_NO_PORTFOLIO_TYPE_DataSupplied() throws Exception {
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		portfolioData.setPortfolioTypeName(null);
		
		this.asertTheBadRequetOfTheCreatePortfolioOfData(portfolioData);
	}
	
	@Test
	void createNewPortfolioOfClient_empty_PORTFOLIO_TYPE_DataSupplied() throws Exception {
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		portfolioData.setPortfolioTypeName("");
		
		this.asertTheBadRequetOfTheCreatePortfolioOfData(portfolioData);
	}
	
	
	@Test
	void createNewPortfolioOfClient_null_balance_DataSupplied() throws Exception {
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		portfolioData.setBalance(null);
		
		this.asertTheBadRequetOfTheCreatePortfolioOfData(portfolioData);
	}
	
	
	
	@Test
	void createNewPortfolioOfClient_negative_balance_DataSupplied() throws Exception {
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		portfolioData.setBalance(BigDecimal.valueOf(-1000));
		
		this.asertTheBadRequetOfTheCreatePortfolioOfData(portfolioData);
	}
	


	@Test
	void createNewPortfolioOfClient_zero_balance_DataSupplied() throws Exception {
		Portfolio portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
		portfolioData.setBalance(BigDecimal.valueOf(0));
		
		this.asertTheBadRequetOfTheCreatePortfolioOfData(portfolioData);
	}
	
	@Test
	void createNewPortfolioOfClient_2_initial_holdings_data_supplied() throws Exception {
		
		
		this.asertTheBadRequetOfTheCreatePortfolioOfData(portfolio2);
	}
	
	
	private void asertTheBadRequetOfTheCreatePortfolioOfData(Portfolio portfolio) {
		String requestUrl="/portfolios/client/{clientId}";
		Map<String,Object> params=new HashMap<>();
		params.put("clientId", clientId);
		
		
		ResponseEntity<String> response=restTemplate.postForEntity(requestUrl,portfolio, String.class, params);
		
		assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
	}
//	
	@Test
	void deleteThePortfolioFromItsId() throws Exception {
		String requestUrl="/portfolios/{portfolioId}";
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolio2.getPortfolioId());
		
		
		ResponseEntity<String> response= restTemplate.exchange(requestUrl, HttpMethod.DELETE	, null, String.class, params);
		
		assertEquals(response.getStatusCode(),HttpStatus.OK);
	
	}

//	
	@Test
	void deleteThePortfolioFromItsId_Bad_Request() throws Exception {
		String requestUrl="/portfolios/{portfolioId}";
		
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "PORTFOLIO");
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolio2.getPortfolioId());
		
		
		ResponseEntity<String> response= restTemplate.exchange(requestUrl, HttpMethod.DELETE	, null, String.class, params);
		
		assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
	
	}
//	
	@Test
	void deleteThePortfolioFromItsId_Server_Error() throws Exception {
		String requestUrl="/portfolios/{portfolioId}";
		
		JdbcTestUtils.dropTables(jdbcTemplate, "TRADE_HISTORY");
		JdbcTestUtils.dropTables(jdbcTemplate, "ORDER_DATA");
		JdbcTestUtils.dropTables(jdbcTemplate, "PORTFOLIO_HOLDING");
		JdbcTestUtils.dropTables(jdbcTemplate, "PORTFOLIO");
		
		Map<String,Object> params=new HashMap<>();
		params.put("portfolioId", portfolio2.getPortfolioId());
		
		
		ResponseEntity<String> response= restTemplate.exchange(requestUrl, HttpMethod.DELETE	, null, String.class, params);
		
		assertEquals(response.getStatusCode(),HttpStatus.INTERNAL_SERVER_ERROR);
	
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
