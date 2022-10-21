package com.fidelity.dao.impl.oracle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fidelity.dao.ClientDao;
import com.fidelity.exceptions.ClientException;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.models.Client;
import com.fidelity.models.ClientIdentification;

@Component("clientDaoOracle")
public class ClientDaoOracleImpl extends ClientDao{
	private DataSource dataSource;

	private final Logger logger = LoggerFactory.getLogger(ClientDao.class);

	public ClientDaoOracleImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public Client registerNewUser(Client client) {
		ClientIdentification[] c = client.getClientIdentification();
		System.out.println(c[0]);
		if(this.getUserByEmail(client.getEmail())!=null) {
			throw new ClientException("Already user exist with this email");
		}
		//Insert into client table & return client;
		String sql1 = "INSERT INTO CLIENT VALUES(?,?,?,?,?,?,?,?,?)";
//		String sql2 = "INSERT INTO INVESTMENT_PREFERENCE VALUES(+,null,null,null,+)";
		try (Connection connection = dataSource.getConnection();
				
				PreparedStatement psmt = connection.prepareStatement(sql1)) {
				// set the parameters
				psmt.setBigDecimal(1, new BigDecimal(client.getClientId()));
				psmt.setString(2, client.getName());
				psmt.setString(3, client.getEmail());
				psmt.setString(4, client.getPostalCode());
				psmt.setString(5, client.getCountry());
				psmt.setString(6, c[0].getType());
				psmt.setString(7, c[0].getValue());
				psmt.setString(8, client.getPassword());
				psmt.setDate(9, java.sql.Date.valueOf(client.getDateOfBirth()));
				// execute the query
				int rs = psmt.executeUpdate();
				
				// handle the result
//				return this.mapTheResultSetTOClient(rs);
				//return new Client(CLIENT_ID,NAME,EMAIL,PASSWORD,POSTAL_CODE,COUNTRY, DOB, null, new ClientIdentification(ID_TYPE,ID_VALUE),RISK_TOLERANCE);
				return client;
			}
		
			//return new Client(rs.CLIENT_ID,NAME,EMAIL,PASSWORD,POSTAL_CODE,COUNTRY, DOB, null, new ClientIdentification(ID_TYPE,ID_VALUE),RISK_TOLERANCE);
			catch (SQLException e) {
				// log the error
				logger.error("Could not get user by id {}", e);
				// throw database exception
				throw new DatabaseException("Could not get user by id");
			}
	}

	@Override
	public Client authenticateUser(String email, String password) {
		// TODO Auto-generated method stub
		Client client = this.getUserByEmail(email);
		if(client.getPassword()==password) {
			return client;
		}
		throw new ClientException("Invalid email or password!!!");
	}

	@Override
	public void removeUserById(BigInteger clientId) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM CLIENT WHERE CLIENT_ID = ?";
		try (Connection connection = dataSource.getConnection();
				
				PreparedStatement psmt = connection.prepareStatement(sql)) {
				// set the parameters
				psmt.setBigDecimal(1, new BigDecimal(clientId));
				// execute the query
				int rs = psmt.executeUpdate();
				// handle the result
//				return this.mapTheResultSetTOClient(rs);
			}
			//return new Client(rs.CLIENT_ID,NAME,EMAIL,PASSWORD,POSTAL_CODE,COUNTRY, DOB, null, new ClientIdentification(ID_TYPE,ID_VALUE),RISK_TOLERANCE);
			catch (SQLException e) {
				// log the error
				logger.error("Could not DELETE user by id {}", e);
				// throw database exception
				throw new DatabaseException("Could not DELETE user by id");
			}
	}

	@Override
	public Client getUserById(BigInteger clientId) {
		// TODO Auto-generated method stub
		String sql = "SELECT C.CLIENT_ID, C.NAME, C.EMAIL, C.PASSWORD, C.POSTAL_CODE, C.COUNTRY, C.DOB, C.ID_TYPE, C.ID_VALUE, I.RISK_TOLERANCE "
				+ "FROM CLIENT C LEFT JOIN INVESTMENT_PREFERENCE I ON I.CLIENT_ID = C.CLIENT_ID"
				+ " WHERE C.CLIENT_ID = ?";
		try (Connection connection = dataSource.getConnection();
			
			PreparedStatement psmt = connection.prepareStatement(sql)) {
			// set the parameters
			psmt.setBigDecimal(1, new BigDecimal(clientId));
			// execute the query
			ResultSet rs = psmt.executeQuery();
			// handle the result
//			return this.mapTheResultSetTOClient(rs);
			if(rs.next()) {
			return new Client(rs.getBigDecimal("CLIENT_ID").toBigInteger(),rs.getString("NAME"),
					rs.getString("EMAIL"),rs.getString("PASSWORD"),
					rs.getString("POSTAL_CODE"),rs.getString("COUNTRY"), null,
					rs.getDate("DOB").toLocalDate(), null,
					new ClientIdentification[] {new ClientIdentification(rs.getString("ID_TYPE"), rs.getString("ID_VALUE"))},
					rs.getString("RISK_TOLERANCE"));
			}
			return null;
		}
		catch (SQLException e) {
			// log the error
			logger.error("Could not get user by id {}", e);
			// throw database exception
			throw new DatabaseException("Could not get user by id");
		}
	}

	@Override
	public Client getUserByEmail(String email) {
		// TODO Auto-generated method stub
		String sql = "SELECT C.CLIENT_ID, C.NAME, C.EMAIL, C.PASSWORD, C.POSTAL_CODE, C.COUNTRY, C.DOB, C.ID_TYPE, C.ID_VALUE, I.RISK_TOLERANCE "
				+ "FROM CLIENT C LEFT JOIN INVESTMENT_PREFERENCE I ON I.CLIENT_ID = C.CLIENT_ID"
				+ " WHERE C.EMAIL = ?";
		try (Connection connection = dataSource.getConnection();
				
				PreparedStatement psmt = connection.prepareStatement(sql)) {
				// set the parameters
				psmt.setString(1, email);
				// execute the query
				ResultSet rs = psmt.executeQuery();
				// handle the result
//				return this.mapTheResultSetTOClient(rs);
				return new Client(rs.getBigDecimal("CLIENT_ID").toBigInteger(),rs.getString("NAME"),
						rs.getString("EMAIL"),rs.getString("PASSWORD"),
						rs.getString("POSTAL_CODE"),rs.getString("COUNTRY"), null,
						rs.getDate("DOB").toLocalDate(), null,
						new ClientIdentification[] {new ClientIdentification(rs.getString("ID_TYPE"), rs.getString("ID_VALUE"))},
						rs.getString("RISK_TOLERANCE"));
			}
			//return new Client(rs.CLIENT_ID,NAME,EMAIL,PASSWORD,POSTAL_CODE,COUNTRY, DOB, null, new ClientIdentification(ID_TYPE,ID_VALUE),RISK_TOLERANCE);
			catch (SQLException e) {
				// log the error
				logger.error("Could not get user by email {}", e);
				// throw database exception
				throw new DatabaseException("Could not get user by email");
			}
	}
}
