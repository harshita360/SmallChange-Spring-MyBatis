package com.fidelity.dao.impl.myBatis;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.dao.ClientDao;
import com.fidelity.exceptions.ClientException;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.models.Client;
import com.fidelity.models.ClientIdentification;

@DisplayName("Client dao MyBatis Implementation")
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:beans.xml")
@Transactional

class ClientDaoMyBatisImplTest {
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ClientDao dao;	
	
	@Autowired
	DataSource datasource;
	
	@BeforeEach
	void setUp(){
		jdbcTemplate = new JdbcTemplate(datasource);
	}
	@DisplayName("Authenticate User Test 1")
	@Test
	void authenticateUserTest() {
		Client successlogin=dao.authenticateUser("nikhil@gmail.com", "NIKHIL@123");
		assertEquals(successlogin.getEmail(), "nikhil@gmail.com");
	}
	
	@DisplayName("Authenticate User Test 2")
	@Test
	void authenticateUserTestFailure() {
		assertThrows(ClientException.class,() -> {
			Client successlogin=dao.authenticateUser("nikhil@gmail.com", "NIKHIL@12");
		});
	}
	
	@Test
	void insertClient() {
		ClientIdentification clientIdentification=new ClientIdentification("a","b");
		List<ClientIdentification> list=new ArrayList<ClientIdentification>();
		assertEquals(0,countRowsInTableWhere(jdbcTemplate, "Client", "client_id = " + BigInteger.valueOf(12)));
		list.add(clientIdentification);
		ClientIdentification[] arr = list.toArray(new ClientIdentification[0]);
		Client client1=new Client(BigInteger.valueOf(12),"Adithya","adi@gmail.com","adithya124","600123","USA","123456",LocalDate.now(),BigInteger.valueOf(12),arr,"investment");
		Client insert=dao.registerNewUser(client1);
		assertEquals(insert.getEmail(), "adi@gmail.com");
		assertEquals(1,countRowsInTableWhere(jdbcTemplate, "Client", "client_id = " + BigInteger.valueOf(12)));
	}
	
	@Test
	void insertClientDuplicateId() {
		ClientIdentification clientIdentification=new ClientIdentification("a","b");
		List<ClientIdentification> list=new ArrayList<ClientIdentification>();
		assertEquals(1,countRowsInTableWhere(jdbcTemplate, "Client", "client_id = " + BigInteger.valueOf(1463465354)));
		list.add(clientIdentification);
		ClientIdentification[] arr = list.toArray(new ClientIdentification[0]);
		Client client1=new Client(BigInteger.valueOf(1463465354),"Adithya","adi@gmail.com","adithya124","600123","USA","123456",LocalDate.now(),BigInteger.valueOf(12),arr,"investment");
		assertThrows(DuplicateKeyException.class,() -> {
			Client insert=dao.registerNewUser(client1);
		});

			}
	
	@Test
	void getUserById() {
		Client getclientId=dao.getUserById(BigInteger.valueOf(1463465354));
		assertEquals(getclientId.getEmail(), "lok@gmail.com");
	}
	
	@Test
	void getUserByIdNegative() {
		Client getclientId=dao.getUserById(BigInteger.valueOf(146346534));
		assertNull(getclientId);
		//assertEquals(getclientId.getClientId(), "lok@gmail.com");
	}
	@Test
	void deleteUserById() {
		assertEquals(1,countRowsInTableWhere(jdbcTemplate, "Client", "client_id = " + BigInteger.valueOf(1463465354)));
		dao.removeUserById(BigInteger.valueOf(1463465354));
		assertEquals(0,countRowsInTableWhere(jdbcTemplate, "Client", "client_id = " + BigInteger.valueOf(1463465354)));
	}
	
	@Test
	void deleteUserByIdNegative() {
		assertEquals(0,countRowsInTableWhere(jdbcTemplate, "Client", "client_id = " + BigInteger.valueOf(146346535)));
		dao.removeUserById(BigInteger.valueOf(146346535));
		assertEquals(0,countRowsInTableWhere(jdbcTemplate, "Client", "client_id = " + BigInteger.valueOf(146346535)));
	}
	@Test
	void getUserByEmail() {
		Client getclientEmail=dao.getUserByEmail("lok@gmail.com");
		assertEquals(getclientEmail.getClientId(),BigInteger.valueOf(1463465354));
	}
	@Test
	void getUserByEmailNegative() {
		Client getclientEmail=dao.getUserByEmail("lok@gmai.com");
		assertNull(getclientEmail);
		//assertEquals(getclientId.getClientId(),BigInteger.valueOf(1463465354));
	}

	

}
