package com.fidelity.smallchangespring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.mappers.PortfolioMapper;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;


@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:beans.xml")
@Transactional
class SmallchangeSpringApplicationTests {
	
	@Autowired
	PortfolioMapper pMappr;

	@Test
	void contextLoads() {
		assertTrue(true,"Must start");
	}
	
//	@Test
//	void test() {
//		System.out.println(pMappr.getPortfolioFromPortfolioId("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"));
//		
//	}
//	
//	@Test
//	public void test2(){
//		//Q345
//		String portfolioId="f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454";
//		String instrumentId="Q345";
//		Map<Object,Object > data=new HashMap<>();
//		data.put("instrumentId", instrumentId);
//		data.put("portfolioId", portfolioId);
//		System.out.println(pMappr.getPortfolioFromPortfolioIdOfInstrument(data));
//	}
//	
//	@Test
//	public void test4(){
//		System.out.println(pMappr.getPortfoliosOfClient(BigInteger.valueOf(346346435)));
//	}
//	
//	@Test
//	void test5() {
//		BigInteger clientId=BigInteger.valueOf(346346435);
//		Portfolio newPortfolio1=new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454",clientId,"BROKERAGE",BigDecimal.valueOf(1200),"NIKHIL FIRST PORTFOLIO",null);
//		System.out.println(pMappr.updatePortfolio(newPortfolio1));
//	}
//
//	@Test
//	void test5a() {
//		BigInteger clientId=BigInteger.valueOf(346346435);
//		Portfolio newPortfolio1=new Portfolio("f8c3de3d-1312-4d7c-a8b0-29f63c4c3454",clientId,"BROKERAGE",BigDecimal.valueOf(1200),"NIKHIL FIRST PORTFOLIO",null);
//		assertEquals(1,pMappr.addNewPortfolio(newPortfolio1));
//	}
//	
//	@Test
//	void test6() {
//		String portfolioId="f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454";
//		PortfolioHoldings hold1new=new PortfolioHoldings("Q345", BigInteger.valueOf(12),BigDecimal.valueOf(890.97),LocalDateTime.of(2000, 9, 29, 23, 59, 59),LocalDateTime.of(1999, 9, 29, 23, 59, 59));
//		Map<Object,Object > data=new HashMap<>();
//		data.put("holding", hold1new);
//		data.put("portfolioId", portfolioId);
//		assertEquals(1,pMappr.updatePortfolioHolding(data));
//	}
//	
//	@Test
//	void test7() {
//		String portfolioId="f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454";
//		PortfolioHoldings hold1new=new PortfolioHoldings("Q3487", BigInteger.valueOf(12),BigDecimal.valueOf(890.97),LocalDateTime.of(2000, 9, 29, 23, 59, 59),LocalDateTime.of(1999, 9, 29, 23, 59, 59));
//		Map<Object,Object > data=new HashMap<>();
//		data.put("holding", hold1new);
//		data.put("portfolioId", portfolioId);
//		assertEquals(1,pMappr.addPortfolioHolding(data));
//	}
//	
//	@Test
//	void test8a() {
//		String portfolioId="f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454";
//		assertEquals(1,pMappr.deletePortfolioByPortfolioId(portfolioId));
//	}
//	
//	@Test
//	void test8b() {
//		BigInteger clientId=BigInteger.valueOf(346346435);
//		assertEquals(2,pMappr.deletePortfoliosOfClient(clientId));
//	}
//	
//	@Test
//	void test9() {
//		String portfolioId="f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454";
//		String instrumentId="Q345";
//		Map<Object,Object > data=new HashMap<>();
//		data.put("instrumentId", instrumentId);
//		data.put("portfolioId", portfolioId);
//		assertEquals(1,pMappr.deletePortfolioHolding(data));
//	}

}
