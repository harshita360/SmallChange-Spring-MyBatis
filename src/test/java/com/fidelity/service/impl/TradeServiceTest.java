package com.fidelity.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.models.Order;
import com.fidelity.models.Trade;
import com.fidelity.service.TradeService;

@SpringBootTest
@Transactional
public class TradeServiceTest {

@Autowired
private TradeService tradeService;

@Autowired
private JdbcTemplate jdbcTemplate;

BigInteger clientId=BigInteger.valueOf(1728765503);
private Order order=new Order( "PQR","B",clientId,"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454","N123456",10,new BigDecimal(10.0));
String token="Bearer eyJhbGciOiJIUzI1NiJ9.eyJST0xFIjoiQ0xJRU5UIiwic3ViIjoiMTcyODc2NTUwMyIsImZtdHNUb2tlbiI6MTcyODY0MjA0NywiZXhwIjoxNjY3ODM5MTc4LCJpYXQiOjE2Njc4MzQ2NTV9.vsc7m_-Q7v9CPc17o6WbvgI1DW3kS7tBSwO0sul5kow";


@Test
void testExecuteOrderSuccess() {
	//ResponseEntity<Trade> responseStatus=tradeService.executeOrder(order, token);
}
	
}
