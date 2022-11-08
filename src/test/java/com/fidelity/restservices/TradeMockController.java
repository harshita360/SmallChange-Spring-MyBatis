//package com.fidelity.restservices;
//
//import static org.hamcrest.Matchers.emptyOrNullString;
//import static org.hamcrest.Matchers.is;
//import static org.junit.jupiter.api.Assertions.assertArrayEquals;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.time.LocalDateTime;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;  
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; 
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fidelity.exceptions.IneligibleOrderException;
//import com.fidelity.exceptions.NotFoundException;
//import com.fidelity.models.Order;
//import com.fidelity.models.Portfolio;
//import com.fidelity.models.Trade;
//import com.fidelity.service.TradeService;
//
//@WebMvcTest(controllers=TradeController.class)
//public class TradeMockController {
//
//	@Autowired
//	MockMvc mockMvc;
//	
//	@MockBean
//	private TradeService  service;
//	BigInteger clientId;
//	
//	Trade t;
//	Order order;
//	
//	String token;
//	
//	
//	@BeforeEach
//	public void setUp() {
//		clientId=BigInteger.valueOf(1728765503);
//		order=new Order( "PQR","B",clientId,"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454","N123456",10,new BigDecimal(10.0));
//	    t=new Trade("ignhbpxjb2-vhr3oruxjkk-cc4oltlt8j","B",order,clientId,"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454","N123456",LocalDateTime.now(),10,new BigDecimal(104.75),new BigDecimal(1057.975));
//	    token="Bearer eyJhbGciOiJIUzI1NiJ9.eyJST0xFIjoiQ0xJRU5UIiwic3ViIjoiMTcyODc2NTUwMyIsImZtdHNUb2tlbiI6MTcyODY0MjA0NywiZXhwIjoxNjY3ODM5MTc4LCJpYXQiOjE2Njc4MzQ2NTV9.vsc7m_-Q7v9CPc17o6WbvgI1DW3kS7tBSwO0sul5kow";
//	}
//	
//	@Test
//	void exceuteOrder() throws Exception  {
//		
//		String requestUrl="/trade";
//		
//		when(service.executeOrder(order,token)).thenReturn(t);
//		
//		MvcResult result=mockMvc.perform(post(requestUrl,order,token))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andReturn();
//
//	}
//	
//	@Test
//	public void executeOrder_Exception() throws Exception {
//		String requestUrl="/portfolios/client/{clientId}";
//		
//		when(service.executeOrder(order,token)).thenThrow(IneligibleOrderException.class);
//		
//		mockMvc.perform(get(requestUrl,clientId))
//		.andDo(print())
//		.andExpect((content().string(is("Not valid order"))));
//	
//	}
//	
//	
//	
//	
//	
//}
