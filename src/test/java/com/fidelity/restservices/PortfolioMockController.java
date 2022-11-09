/*
 * package com.fidelity.restservices;
 * 
 * import static org.junit.jupiter.api.Assertions.assertArrayEquals; import
 * static org.junit.jupiter.api.Assertions.assertEquals; import static
 * org.mockito.Mockito.doThrow; import static org.mockito.Mockito.when; import
 * static
 * org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
 * import static
 * org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
 * import static
 * org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
 * import static
 * org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
 * import static
 * org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 * 
 * import java.math.BigDecimal; import java.math.BigInteger; import
 * java.time.LocalDateTime; import java.util.ArrayList; import java.util.List;
 * import java.util.UUID;
 * 
 * import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;
 * import org.mockito.invocation.InvocationOnMock; import
 * org.mockito.stubbing.Answer; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Qualifier; import
 * org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; import
 * org.springframework.boot.test.mock.mockito.MockBean; import
 * org.springframework.http.MediaType; import
 * org.springframework.test.context.ActiveProfiles; import
 * org.springframework.test.web.servlet.MockMvc; import
 * org.springframework.test.web.servlet.MvcResult;
 * 
 * import com.fasterxml.jackson.databind.ObjectMapper; import
 * com.fidelity.exceptions.DatabaseException; import
 * com.fidelity.exceptions.NotEligibleException; import
 * com.fidelity.exceptions.NotFoundException; import
 * com.fidelity.models.Portfolio; import com.fidelity.models.PortfolioHoldings;
 * import com.fidelity.service.PortfolioService; import
 * com.fidelity.utils.PortfolioUtils;
 * 
 * @WebMvcTest(controllers=PortfolioController.class)
 * 
 * @ActiveProfiles("my-batis") public class PortfolioMockController {
 * 
 * @Autowired MockMvc mockMvc;
 * 
 * // @Qualifier("mainPortfolioService") // @MockBean // private
 * PortfolioService service;
 * 
 * @Qualifier("proxyPortfolioService")
 * 
 * @MockBean private PortfolioService service;
 * 
 * Portfolio portfolio1; Portfolio portfolio2; List<Portfolio> portfolios;
 * BigInteger clientId;
 * 
 * @BeforeEach public void setUp() { clientId=BigInteger.valueOf(346346435);
 * portfolio1=new Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3121",clientId,
 * "BROKERAGE-B",BigDecimal.valueOf(10000),"NIKHIL Second PORTFOLIO", new
 * ArrayList<>() ); List<PortfolioHoldings> holdings=new ArrayList<>();
 * holdings.add(new PortfolioHoldings("Q347", BigInteger.valueOf(10),
 * BigDecimal.valueOf(876.97), LocalDateTime.of(1999, 9, 29, 23, 29,29),
 * LocalDateTime.of(1999, 9, 29, 23, 29,29))); portfolio2=new
 * Portfolio("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454",clientId,
 * "BROKERAGE",BigDecimal.valueOf(10000),"NIKHIL FIRST PORTFOLIO", holdings );
 * 
 * portfolios=new ArrayList<>(); portfolios.add(portfolio1);
 * portfolios.add(portfolio2);
 * 
 * }
 * 
 * @Test void getAllUserPortfolios() throws Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}";
 * 
 * when(service.getPortfoliosForAUser(clientId)).thenReturn(portfolios);
 * 
 * MvcResult result=mockMvc.perform(get(requestUrl,clientId)) .andDo(print())
 * .andExpect(status().isOk()) .andReturn();
 * 
 * 
 * Portfolio[] retrived=new
 * ObjectMapper().readValue(result.getResponse().getContentAsString(),
 * Portfolio[].class); assertArrayEquals(portfolios.toArray(), retrived); }
 * 
 * @Test public void getAllUserPortfolios_Not_Found() throws Exception { String
 * requestUrl="/portfolios/client/{clientId}";
 * 
 * when(service.getPortfoliosForAUser(clientId)).thenThrow(NotFoundException.
 * class);
 * 
 * mockMvc.perform(get(requestUrl,clientId)) .andDo(print())
 * .andExpect(status().isNotFound());
 * 
 * }
 * 
 * @Test public void getAllUserPortfolios_Bad_Request() throws Exception {
 * String requestUrl="/portfolios/client/{clientId}";
 * 
 * when(service.getPortfoliosForAUser(clientId)).thenThrow(DatabaseException.
 * class);
 * 
 * mockMvc.perform(get(requestUrl,clientId)) .andDo(print())
 * .andExpect(status().isBadRequest());
 * 
 * }
 * 
 * @Test public void getAllUserPortfolios_Server_Error() throws Exception {
 * String requestUrl="/portfolios/client/{clientId}";
 * 
 * when(service.getPortfoliosForAUser(clientId)).thenThrow(RuntimeException.
 * class);
 * 
 * mockMvc.perform(get(requestUrl,clientId)) .andDo(print())
 * .andExpect(status().is5xxServerError());
 * 
 * }
 * 
 * @Test public void getAllUserPortfolios_Empty_List() throws Exception { String
 * requestUrl="/portfolios/client/{clientId}";
 * 
 * when(service.getPortfoliosForAUser(clientId)).thenReturn(new ArrayList<>());
 * 
 * mockMvc.perform(get(requestUrl,clientId)) .andDo(print())
 * .andExpect(status().isNoContent());
 * 
 * }
 * 
 * @Test public void getThePortfolioFromItsId() throws Exception { String
 * requestUrl="/portfolios/{portfolioId}";
 * 
 * when(service.getPortfolioForAuserFromPortfolioId(portfolio2.getPortfolioId())
 * ).thenReturn(portfolio2);
 * 
 * MvcResult result=mockMvc.perform(get(requestUrl,portfolio2.getPortfolioId()))
 * .andDo(print()) .andExpect(status().isOk()) .andReturn();
 * 
 * 
 * Portfolio retrived=new
 * ObjectMapper().readValue(result.getResponse().getContentAsString(),
 * Portfolio.class); assertEquals(portfolio2, retrived);
 * 
 * }
 * 
 * // @Test // public void getThePortfolioFromItsId_Bad_Request() throws
 * Exception { // String requestUrl="/portfolios/{portfolioId}"; // //
 * when(service.getPortfolioForAuserFromPortfolioId(portfolio2.getPortfolioId())
 * ).thenThrow(DatabaseException.class); // //
 * mockMvc.perform(get(requestUrl,portfolio2.getPortfolioId())) //
 * .andDo(print()) // .andExpect(status().isBadRequest()); // // }
 * 
 * @Test void getThePortfolioFromItsId_Server_Error() throws Exception { String
 * requestUrl="/portfolios/{portfolioId}";
 * 
 * when(service.getPortfolioForAuserFromPortfolioId(portfolio2.getPortfolioId())
 * ).thenThrow(RuntimeException.class);
 * 
 * mockMvc.perform(get(requestUrl,portfolio2.getPortfolioId())) .andDo(print())
 * .andExpect(status().is5xxServerError());
 * 
 * }
 * 
 * @Test void createDefaultPortfolioOfClient() throws Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}/default"; Portfolio
 * portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
 * 
 * when(service.addNewPortfolio(portfolioData)).thenAnswer(new
 * Answer<Portfolio>() {
 * 
 * @Override public Portfolio answer(InvocationOnMock invocation) throws
 * Throwable { Portfolio portfolio= invocation.getArgument(0);
 * System.out.println("In Anwer");
 * portfolio.setPortfolioId(UUID.randomUUID().toString()); return portfolio; }
 * 
 * });
 * 
 * MvcResult result=mockMvc.perform(post(requestUrl,clientId)) .andDo(print())
 * .andExpect(status().isCreated()) .andReturn();
 * 
 * 
 * Portfolio retrived=new
 * ObjectMapper().readValue(result.getResponse().getContentAsString(),
 * Portfolio.class); portfolioData.setPortfolioId(retrived.getPortfolioId());
 * assertEquals(portfolioData, retrived); }
 * 
 * @Test void createDefaultPortfolioOfClient_Not_Found() throws Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}/default"; Portfolio
 * portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
 * 
 * when(service.addNewPortfolio(portfolioData)).thenThrow(NotFoundException.
 * class); mockMvc.perform(post(requestUrl,clientId)) .andDo(print())
 * .andExpect(status().isNotFound()); }
 * 
 * @Test void createDefaultPortfolioOfClient_Server_Error() throws Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}/default"; Portfolio
 * portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
 * 
 * when(service.addNewPortfolio(portfolioData)).thenThrow(RuntimeException.class
 * ); mockMvc.perform(post(requestUrl,clientId)) .andDo(print())
 * .andExpect(status().is5xxServerError()); }
 * 
 * @Test void createDefaultPortfolioOfClient_Database_Error() throws Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}/default"; Portfolio
 * portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
 * 
 * when(service.addNewPortfolio(portfolioData)).thenThrow(DatabaseException.
 * class); mockMvc.perform(post(requestUrl,clientId)) .andDo(print())
 * .andExpect(status().isBadRequest()); }
 * 
 * @Test void createDefaultPortfolioOfClient_NotCorrectDataSupplied() throws
 * Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}/default"; Portfolio
 * portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
 * 
 * when(service.addNewPortfolio(portfolioData)).thenThrow(NotEligibleException.
 * class); mockMvc.perform(post(requestUrl,clientId)) .andDo(print())
 * .andExpect(status().isBadRequest()); }
 * 
 * @Test void createNewPortfolioOfClient() throws Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}"; Portfolio
 * portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
 * 
 * when(service.addNewPortfolio(portfolioData)).thenAnswer(new
 * Answer<Portfolio>() {
 * 
 * @Override public Portfolio answer(InvocationOnMock invocation) throws
 * Throwable { Portfolio portfolio= invocation.getArgument(0);
 * //System.out.println("In Anwer");
 * portfolio.setPortfolioId(UUID.randomUUID().toString()); return portfolio; }
 * 
 * });
 * 
 * ObjectMapper mapper=new ObjectMapper();
 * 
 * MvcResult result=mockMvc.perform(post(requestUrl,clientId)
 * .contentType(MediaType.APPLICATION_JSON_VALUE)
 * .content(mapper.writeValueAsString(portfolioData)) ) .andDo(print())
 * .andExpect(status().isCreated()) .andReturn();
 * 
 * 
 * Portfolio retrived=new
 * ObjectMapper().readValue(result.getResponse().getContentAsString(),
 * Portfolio.class); portfolioData.setPortfolioId(retrived.getPortfolioId());
 * assertEquals(portfolioData, retrived); }
 * 
 * @Test void createNewPortfolioOfClient_Not_Found() throws Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}"; Portfolio
 * portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
 * 
 * when(service.addNewPortfolio(portfolioData)).thenThrow(NotFoundException.
 * class);
 * 
 * ObjectMapper mapper=new ObjectMapper();
 * 
 * mockMvc.perform(post(requestUrl,clientId)
 * .contentType(MediaType.APPLICATION_JSON_VALUE)
 * .content(mapper.writeValueAsString(portfolioData)) ) .andDo(print())
 * .andExpect(status().isNotFound()); }
 * 
 * @Test void createNewPortfolioOfClient_Server_Error() throws Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}"; Portfolio
 * portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
 * 
 * when(service.addNewPortfolio(portfolioData)).thenThrow(RuntimeException.class
 * );
 * 
 * ObjectMapper mapper=new ObjectMapper();
 * 
 * mockMvc.perform(post(requestUrl,clientId)
 * .contentType(MediaType.APPLICATION_JSON_VALUE)
 * .content(mapper.writeValueAsString(portfolioData)) ) .andDo(print())
 * .andExpect(status().is5xxServerError()); }
 * 
 * @Test void createNewPortfolioOfClient_Database_Error() throws Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}"; Portfolio
 * portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
 * 
 * when(service.addNewPortfolio(portfolioData)).thenThrow(DatabaseException.
 * class);
 * 
 * ObjectMapper mapper=new ObjectMapper();
 * 
 * mockMvc.perform(post(requestUrl,clientId)
 * .contentType(MediaType.APPLICATION_JSON_VALUE)
 * .content(mapper.writeValueAsString(portfolioData)) ) .andDo(print())
 * .andExpect(status().isBadRequest()); }
 * 
 * @Test void createNewPortfolioOfClient_NotCorrectDataSupplied() throws
 * Exception {
 * 
 * String requestUrl="/portfolios/client/{clientId}"; Portfolio
 * portfolioData=PortfolioUtils.getDefaultPortfolio(clientId);
 * 
 * when(service.addNewPortfolio(portfolioData)).thenThrow(NotEligibleException.
 * class);
 * 
 * ObjectMapper mapper=new ObjectMapper();
 * 
 * mockMvc.perform(post(requestUrl,clientId)
 * .contentType(MediaType.APPLICATION_JSON_VALUE)
 * .content(mapper.writeValueAsString(portfolioData)) ) .andDo(print())
 * .andExpect(status().isBadRequest()); }
 * 
 * @Test public void deleteThePortfolioFromItsId() throws Exception { String
 * requestUrl="/portfolios/{portfolioId}";
 * 
 * when(service.getPortfolioForAuserFromPortfolioId(portfolio2.getPortfolioId())
 * ).thenReturn(portfolio2);
 * 
 * mockMvc.perform(delete(requestUrl,portfolio2.getPortfolioId()))
 * .andDo(print()) .andExpect(status().isOk());
 * 
 * }
 * 
 * // @Test // public void deleteThePortfolioFromItsId_Not_Found() throws
 * Exception { // String requestUrl="/portfolios/{portfolioId}"; // //
 * doThrow(NotFoundException.class).when(service).deletePortfolioById(portfolio2
 * .getPortfolioId()); // //
 * mockMvc.perform(delete(requestUrl,portfolio2.getPortfolioId())) //
 * .andDo(print()) // .andExpect(status().isNotFound()); // // }
 * 
 * @Test public void deleteThePortfolioFromItsId_Bad_Request() throws Exception
 * { String requestUrl="/portfolios/{portfolioId}";
 * 
 * doThrow(DatabaseException.class).when(service).deletePortfolioById(portfolio2
 * .getPortfolioId());
 * 
 * mockMvc.perform(delete(requestUrl,portfolio2.getPortfolioId()))
 * .andDo(print()) .andExpect(status().isBadRequest());
 * 
 * }
 * 
 * @Test void deleteThePortfolioFromItsId_Server_Error() throws Exception {
 * String requestUrl="/portfolios/{portfolioId}";
 * 
 * doThrow(RuntimeException.class).when(service).deletePortfolioById(portfolio2.
 * getPortfolioId());
 * mockMvc.perform(delete(requestUrl,portfolio2.getPortfolioId()))
 * .andDo(print()) .andExpect(status().is5xxServerError());
 * 
 * }
 * 
 * 
 * 
 * }
 */