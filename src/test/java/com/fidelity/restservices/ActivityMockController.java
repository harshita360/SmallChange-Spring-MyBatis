package com.fidelity.restservices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.exceptions.NotFoundException;
import com.fidelity.models.Order;
import com.fidelity.models.Trade;
import com.fidelity.service.ActivityService;

@WebMvcTest(controllers=ActivityController.class)
@ActiveProfiles("my-batis")
class ActivityMockController {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	private ActivityService service;
	
	BigInteger clientId;
	Order order;
	Trade trade;
	String portfolioId;
	List<Trade> activity;
	
	@BeforeEach
	void setUp() {
		clientId=BigInteger.valueOf(346346435);
		portfolioId="f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454";
		Order order = new Order("UUTT789", "S", clientId, portfolioId, "Q345", 10,
				new BigDecimal(100));
		trade = new Trade("TRADE_ID-1", order.getDirection(), order, order.getClientId(), order.getPortfolioId(),
				order.getInstrumentId(), null, order.getQuantity(),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())),
				order.getTargetPrice().multiply(new BigDecimal(order.getQuantity())).subtract(new BigDecimal(3)));
		activity=new ArrayList<>();
		activity.add(trade);
	}
	
	@Test
	void getUserActivityTest() throws Exception {
		
		String requestUrl="/activity/client/{clientId}";
		
		when(service.getUserActivity(clientId)).thenReturn(activity);
		
		MvcResult result=mockMvc.perform(get(requestUrl,clientId))
		.andDo(print())
		.andExpect(status().isOk())
		.andReturn();
		
		
		Trade[] retrived=new ObjectMapper().readValue(result.getResponse().getContentAsString(), Trade[].class);
		assertArrayEquals(activity.toArray(), retrived);
	}
	
	@Test
	void getUserActivity_Not_Found() throws Exception {
		String requestUrl="/activity/client/{clientId}";
		
		when(service.getUserActivity(clientId)).thenThrow(NotFoundException.class);
		
		mockMvc.perform(get(requestUrl,clientId))
		.andDo(print())
		.andExpect(status().isNotFound());
	
	}
	
	@Test
	void getUserActivity_Bad_Request() throws Exception {
		String requestUrl="/activity/client/{clientId}";
		
		when(service.getUserActivity(clientId)).thenThrow(DatabaseException.class);
		
		mockMvc.perform(get(requestUrl,clientId))
		.andDo(print())
		.andExpect(status().isBadRequest());
	
	}
	
	@Test
	void getUserActivity_Server_Error() throws Exception {
		String requestUrl="/activity/client/{clientId}";
		
		when(service.getUserActivity(clientId)).thenThrow(RuntimeException.class);
		
		mockMvc.perform(get(requestUrl,clientId))
		.andDo(print())
		.andExpect(status().is5xxServerError());
	
	}
	
	@Test
	void getUserActivity_Empty_List() throws Exception {
		String requestUrl="/activity/client/{clientId}";
		
		when(service.getUserActivity(clientId)).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get(requestUrl,clientId))
		.andDo(print())
		.andExpect(status().isNoContent());
	
	}
	
	@Test
	void getPortfolioActivityTest() throws Exception {
		
		String requestUrl="/activity/{portfolioId}";
		
		when(service.getPortfolioActivity(portfolioId)).thenReturn(activity);
		
		MvcResult result=mockMvc.perform(get(requestUrl,portfolioId))
		.andDo(print())
		.andExpect(status().isOk())
		.andReturn();
		
		
		Trade[] retrived=new ObjectMapper().readValue(result.getResponse().getContentAsString(), Trade[].class);
		assertArrayEquals(activity.toArray(), retrived);
	}
	
	
	@Test
	void getPortfolioActivity_Not_Found() throws Exception {
		String requestUrl="/activity/{portfolioId}";
		
		when(service.getPortfolioActivity(portfolioId)).thenThrow(NotFoundException.class);
		
		mockMvc.perform(get(requestUrl,portfolioId))
		.andDo(print())
		.andExpect(status().isNotFound());
	
	}
	
	@Test
	void getPortfolioActivity_Bad_Request() throws Exception {
		String requestUrl="/activity/{portfolioId}";
		
		when(service.getPortfolioActivity(portfolioId)).thenThrow(DatabaseException.class);
		
		mockMvc.perform(get(requestUrl,portfolioId))
		.andDo(print())
		.andExpect(status().isBadRequest());
	
	}
	
	@Test
	void getPortfolioActivity_Server_Error() throws Exception {
		String requestUrl="/activity/{portfolioId}";
		
		when(service.getPortfolioActivity(portfolioId)).thenThrow(RuntimeException.class);
		
		mockMvc.perform(get(requestUrl,portfolioId))
		.andDo(print())
		.andExpect(status().is5xxServerError());
	
	}
	
	@Test
	void getPortfolioActivity_Empty_List() throws Exception {
		String requestUrl="/activity/{portfolioId}";
		
		when(service.getPortfolioActivity(portfolioId)).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get(requestUrl,portfolioId))
		.andDo(print())
		.andExpect(status().isNoContent());
	
	}

}
