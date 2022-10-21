package com.fidelity.integration;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fidelity.exceptions.DatabaseException;

@Component
public class TransactionManager {

	private final Logger logger = LoggerFactory.getLogger(TransactionManager.class);

	private DataSource dataSource;
	private Connection connection;

	@Autowired
	public TransactionManager(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void startTransaction() {
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			logger.error("Unable to begin transaction ,e");
			throw new DatabaseException("Unable to begin a transaction", e);
		}
	}

	public void rollbackTransaction() {

		try {
			connection.rollback();
		} catch (SQLException e) {
			logger.error("Unable to rollback transaction ,e");
			throw new DatabaseException("Unable to rollback a transaction", e);
		}
	}

	public void commitTransaction() {
		try {
			connection.commit();
		} catch (SQLException e) {
			logger.error("Unable to commit transaction ,e");
			throw new DatabaseException("Unable to commit a transaction", e);
		}
	}

}
