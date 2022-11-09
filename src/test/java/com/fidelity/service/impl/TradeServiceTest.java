package com.fidelity.service.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.exceptions.IneligibleOrderException;
import com.fidelity.models.Client;
import com.fidelity.models.Order;
import com.fidelity.models.Trade;
import com.fidelity.service.ClientService;
import com.fidelity.service.TradeService;
import com.fidelity.utils.AuthenticationData;
import com.fidelity.utils.TokenDto;

@SpringBootTest
@Sql(scripts={"classpath:schema.sql", "classpath:data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)  
@Sql(scripts={"classpath:drop.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Transactional
public class TradeServiceTest {

@Autowired
private TradeService tradeService;

@Autowired
private JdbcTemplate jdbcTemplate;

@Autowired
private ClientService clientservice;

BigInteger clientId=BigInteger.valueOf(1728765503);
private Order order=new Order( "PQR","B",clientId,"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3414","N123456",10,new BigDecimal(104.0));
private Order order1=new Order( "PQR","S",clientId,"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3414","N123456",2,new BigDecimal(104.0));
private Order order2=new Order( "PQR","B",clientId,"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3414","N123456",600,new BigDecimal(104.0));

HttpHeaders header;
String token;
@BeforeEach
void setUp() {
	header = new HttpHeaders();
	token = clientservice.authenticateUser("nik@gmail.com","NIKHIL@123").getToken();
	System.out.println(token);
	
}

@Test
void testExecuteOrderSuccess_Buy() throws Exception {
	
	int oldOrderCount = countRowsInTable(jdbcTemplate, "ORDER_DATA");
	System.out.println(token);
	Trade response=tradeService.executeOrder(order,token);
	assertNotNull(response);
	int newOrderCount = countRowsInTable(jdbcTemplate, "ORDER_DATA");
	assertEquals(newOrderCount,oldOrderCount+1);
	
	
}

@Test
void testExecuteOrder_Sell() throws Exception {
	
	int oldOrderCount = countRowsInTable(jdbcTemplate, "ORDER_DATA");
	System.out.println(token);
	Trade response=tradeService.executeOrder(order,token);
	Trade response2=tradeService.executeOrder(order1,token);
	assertNotNull(response2);
	int newOrderCount = countRowsInTable(jdbcTemplate, "ORDER_DATA");
	assertEquals(newOrderCount,oldOrderCount+2);
	
	
}
	
@Test
void testExcecuteOrder_Exception() throws Exception{
	assertThrows(IneligibleOrderException.class,() -> {
		Trade response=tradeService.executeOrder(order2,token);
	});
	
}
}
