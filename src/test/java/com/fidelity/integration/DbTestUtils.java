package com.fidelity.integration;
/*
 * DbTestUtils.java - utility functions for database integration tests.
 */

import java.sql.Connection;

import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;


public class DbTestUtils
{

	private static final String SQL_SCRIPT = "sql/emp_populate.sql";

	private Connection connection;

	public DbTestUtils(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Re-run the database initialization script.
	 * Only pure SQL scripts allowed - PL/SQL statements must be removed
	 * 
	 * Alternatively, for simple schemas, we could drop all rows in the required
	 * tables, then insert test data: Statement stmt = connection.createStatement();
	 * stmt.executeUpdate("delete from emp"); stmt.executeUpdate("insert into emp
	 * (empno,ename,job,...) values (7369,'SMITH','CLERK',...)"); ... stmt.close();
	 */
	public void initDb() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		
		populator.setContinueOnError(true);
		populator.addScript(new FileSystemResource(SQL_SCRIPT));
		populator.populate(connection);
	}

	public JdbcTemplate initJdbcTemplate() {
		return new JdbcTemplate(new SingleConnectionDataSource(connection, true));
	}

	/**
	 * Assert a Employee instance has the same property values as the
	 * columnValueMap.
	 * 
	 * @param employee           a Employee instance
	 * @param employeeProperties a Map returned by JdbcTemplate.queryForMap. The key
	 *                           for each item is a column name, and the value is
	 *                           the column's value.
	 */
//	public void assertEmployeeEquals(Employee employee, Map<String, Object> employeeProperties) {
//		assertEquals(employee.getEmpNumber(), intValue(employeeProperties.get("empno")));
//		assertEquals(employee.getEmpName(), employeeProperties.get("ename"));
//		assertEquals(employee.getJob(), employeeProperties.get("job"));
//		assertEquals(employee.getMgrNumber(), intValue(employeeProperties.get("mgr")));
//		assertEquals(employee.getHireDate(), ((Timestamp) employeeProperties.get("hiredate")).toLocalDateTime().toLocalDate());
//		assertEquals(employee.getSalary().compareTo((BigDecimal)employeeProperties.get("sal")), 0);
//		assertEquals(employee.getComm().compareTo((BigDecimal) employeeProperties.get("comm")), 0);
//		assertEquals(employee.getDeptNumber(), intValue(employeeProperties.get("deptno")));
//	}
//
//	public void assertEmployeeEquals(List<Employee> employees, List<Map<String, Object>> employeeProperties) {
//		assertEquals(employees.size(), employeeProperties.size());
//		for (int i = 0; i < employees.size(); i++) {
//			assertEmployeeEquals(employees.get(i), employeeProperties.get(i));
//		}
//	}

	/**
	 * Implements some basic sanity checking for an Employee
	 * 
	 * @param employee
	 * @return true if all conditions are met
	 */

//	public boolean validateEmployee(Employee employee) {
//		boolean valid = 
//				employee.getEmpNumber() > 0
//			 && Objects.nonNull(employee.getEmpName())
//			 && Objects.nonNull(employee.getJob())
//			 && employee.getMgrNumber() >= 0
//			 && Objects.nonNull(employee.getHireDate())
//			 && employee.getSalary().compareTo(BigDecimal.ZERO) > 0
//			 && employee.getComm().compareTo(BigDecimal.ZERO) > 0
//			 && employee.getDeptNumber() > 0;
//		
//		return valid;
//	}

//	private int intValue(Object bigDecimal) {
//		return bigDecimal != null ? ((BigDecimal) bigDecimal).intValue() : 0;
//	}
//
//	private double doubleValue(Object bigDecimal) {
//		return bigDecimal != null ? ((BigDecimal) bigDecimal).doubleValue() : 0;
//	}
//
//	private String formatTimestamp(Object timestamp) {
//		Date date = new Date(((Timestamp) timestamp).getTime());
//		return new SimpleDateFormat("dd-MMM-yyyy").format(date).toUpperCase();
//	}

}
