package com.fidelity.dao.impl.oracle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fidelity.dao.PortfolioDao;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;

@Component("portfolioDaoOracle")
public class PortfolioDaoOracleImp extends PortfolioDao {

	private DataSource dataSource;

	private final Logger logger = LoggerFactory.getLogger(PortfolioDao.class);

	@Autowired
	public PortfolioDaoOracleImp(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	@Override
	public List<Portfolio> getPortfoliosForAUser(BigInteger clientId) {
		// sql statement to get all cleint portfolios
		String sql = """
				SELECT
				    p.portfolio_id,
				    p.name,
				    p.p_category,
				    p.balance,
				    p.client_id,
				    ph.instrument_id,
				    ph.investment_price,
				    ph.quantity ,
				    ph.added_at ,
				    ph.last_update_at
				FROM PORTFOLIO P LEFT OUTER JOIN portfolio_holding PH ON p.portfolio_id=ph.portfolio_id
				WHERE p.client_id=?
				ORDER BY p.portfolio_id, ph.last_update_at DESC
				""";
		// try to get the connection and prepare the statements to execute sql statement
		try (Connection connection = dataSource.getConnection();
				PreparedStatement psmt = connection.prepareStatement(sql)) {
			// set the parameters
			psmt.setBigDecimal(1, new BigDecimal(clientId));
			// execute the query
			ResultSet rs = psmt.executeQuery();
			// handle the result
			return this.mapTheResultSetTOlistPortfolios(rs);
		}
		// catch sqlException
		catch (SQLException e) {
			// log the error
			logger.error("Could not retrive client portfolios {}", sql, e);
			// throw database exception
			throw new DatabaseException("Could not retrive user portfolios");
		}
	}

	@Override
	public Portfolio getPortfolioForAuserFromPortfolioId(String portfolioId) {
		// sql statement to get portfolio by its id
		String sql = """
				SELECT
				    p.portfolio_id,
				    p.name,
				    p.p_category,
				    p.balance,
				    p.client_id,
				    ph.instrument_id,
				    ph.investment_price,
				    ph.quantity ,
				    ph.added_at ,
				    ph.last_update_at
				FROM PORTFOLIO P LEFT OUTER JOIN portfolio_holding PH ON p.portfolio_id=ph.portfolio_id
				WHERE p.portfolio_id=?
				""";
		// try to get the connection and prepare the statements to execute sql statement
		try (Connection connection = dataSource.getConnection();
				PreparedStatement psmt = connection.prepareStatement(sql)) {
			// set the parameters
			psmt.setString(1, portfolioId);
			// execute the query
			ResultSet rs = psmt.executeQuery();
			// handle the result
			List<Portfolio> portfolios = this.mapTheResultSetTOlistPortfolios(rs);
			if (portfolios.size() == 1) {
				return portfolios.get(0);
			}
			return null;
		}
		// catch sqlException
		catch (SQLException e) {
			// log the error
			logger.error("Could not retrive client portfolios {}", sql, e);
			// throw database exception
			throw new DatabaseException("Could not retrive user portfolios");
		}
	}

	@Override
	public Portfolio addNewPortfolio(Portfolio portfolio) {
		String sql = "INSERT INTO " + " PORTFOLIO(PORTFOLIO_ID,CLIENT_ID,BALANCE,NAME,P_CATEGORY)"
				+ " VALUES(?,?,?,?,?)";
		// "INSERT INTO
		// PORTFOLIO_HOLDING(PORTFOLIO_ID,INSTRUMENT_ID,QUANTITY,INVESTMENT_PRICE,ADDED_AT,
		// LAST_UPDATE_AT) "
		// + " VALUES(?,?,?,?,?,?) "
		// + " ";

		// try to get the connection and prepared statement
		try (Connection con = dataSource.getConnection(); PreparedStatement psmt = con.prepareStatement(sql)) {

			// set the parameters
			psmt.clearParameters();
			psmt.setString(1, portfolio.getPortfolioId());
			psmt.setBigDecimal(2, new BigDecimal(portfolio.getClientId()));
			psmt.setBigDecimal(3, portfolio.getBalance());
			psmt.setString(4, portfolio.getPortfolioName());
			psmt.setString(5, portfolio.getPortfolioTypeName());

			// execute the qquery
			int temp = psmt.executeUpdate();

			// if the new row isn not inserted, then throw a new dataabse exception
			if (temp == 0) {
				throw new DatabaseException("Could not add client portfolio");
			}

			return portfolio;

			// catch the error
		} catch (SQLException e) {
			// log the error
			logger.error("Could not add new client portfolio {}", sql, e);
			// throw database exception
			throw new DatabaseException("Could not add new client portfolio");
		}
	}

	@Override
	public void deletePortfolioById(String portfolioID) {
		// Sql statement to delete portfolio from its ID
		String sql = """
				DELETE FROM portfolio WHERE PORTFOLIO_ID=?
				""";
		// try to get the connection and prepare the statement
		try (Connection connection = dataSource.getConnection();
				PreparedStatement psmt = connection.prepareStatement(sql)) {
			// set the parameters
			psmt.setString(1, portfolioID);
			// execute the update
			int temp = psmt.executeUpdate();
			// if the number of reflected rows is 0, then throwexception
			if (temp == 0) {
				throw new DatabaseException("Could not delete all user portfolios");
			}
		}
		// catch the SQL Exception
		catch (SQLException e) {
			// log the error
			logger.error("Could not delete all user portfolios {}", sql, e);
			// throw database exception
			throw new DatabaseException("Could not delete all user portfolios");
		}

	}

	@Override
	public void deletePortfolioByClientId(BigInteger clientId) {
		// Sql statement to delete all the user portfolios
		String sql = """
				DELETE FROM portfolio WHERE CLIENT_ID=?
				""";
		// try to get the connection and prepare the statement
		try (Connection connection = dataSource.getConnection();
				PreparedStatement psmt = connection.prepareStatement(sql)) {
			// set the parameters
			psmt.setBigDecimal(1, new BigDecimal(clientId));
			// execute the update
			int temp = psmt.executeUpdate();
			// if the number of reflected rows is 0, then throwexception
			if (temp == 0) {
				throw new DatabaseException("Could not delete all user portfolios");
			}
		}
		// catch the SQL Exception
		catch (SQLException e) {
			// log the error
			logger.error("Could not delete all user portfolios {}", sql, e);
			// throw database exception
			throw new DatabaseException("Could not delete all user portfolios");
		}
	}

	@Override
	public Portfolio getPortfolioFromIdAndLoadOfInstrument(String portfolioId, String instrumentId) {
		
		// try to get the connection and prepare the statements to execute sql statement
		try (Connection connection = dataSource.getConnection()) {
			
			return this.getPortfolioFromIdAndLoadOfInstrument(portfolioId, instrumentId, connection);
		}
		// catch sqlException
		catch (SQLException e) {
			// log the error
			logger.error("Could not retrive client portfolios {}", e);
			// throw database exception
			throw new DatabaseException("Could not retrive user portfolios");
		}
	}
	
	private Portfolio getPortfolioFromIdAndLoadOfInstrument(String portfolioId, String instrumentId,Connection connection) {
		// sql statement to get portfolio by its id
		String sql = """
				SELECT
				    p.portfolio_id,
				    p.name,
				    p.p_category,
				    p.balance,
				    p.client_id,
				    ph.instrument_id,
				    ph.investment_price,
				    ph.quantity ,
				    ph.added_at ,
				    ph.last_update_at
				FROM PORTFOLIO P LEFT OUTER JOIN portfolio_holding PH ON p.portfolio_id=ph.portfolio_id AND  ph.instrument_id=?
				WHERE p.portfolio_id=? 
				""";
		// try to get the connection and prepare the statements to execute sql statement
		try (PreparedStatement psmt = connection.prepareStatement(sql)) {
			// set the parameters
			psmt.setString(2, portfolioId);
			psmt.setString(1, instrumentId);
			// execute the query
			ResultSet rs = psmt.executeQuery();
			// handle the result
			List<Portfolio> portfolios = this.mapTheResultSetTOlistPortfolios(rs);
			if (portfolios.size() == 1) {
				return portfolios.get(0);
			}
			return null;
		}
		// catch sqlException
		catch (SQLException e) {
			// log the error
			logger.error("Could not retrive client portfolios {}", sql, e);
			// throw database exception
			throw new DatabaseException("Could not retrive client portfolios");
		}
	}

	@Override
	public Portfolio updatePortfolioFromIdAndLoadOfInstrument(Portfolio portfolio, String instrumentId) {
		
		// try to get the database connection
		try(Connection connection=dataSource.getConnection()){
			// get the current portfolio of the given id from the database
			Portfolio oldPortfolio=this.getPortfolioFromIdAndLoadOfInstrument(portfolio.getPortfolioId(), instrumentId, connection);
			if(oldPortfolio==null) {
				throw new DatabaseException("Portfolio does not exist");
			}
			// update the portfolio details
			this.updateThePortfolio(portfolio, connection);
			
			// if the new pprtfolio has an has new holding
			if( oldPortfolio.getHoldings().size()==0 && portfolio.getHoldings().size()==1 ) {
				// add the instrument holding
				this.addThePortfolioHolding(portfolio.getPortfolioId(), portfolio.getHoldings().get(0), connection);
			}
			// if the retrived and updated data has a single portfolio holding of thst instrument
			else if( oldPortfolio.getHoldings().size()==1 && portfolio.getHoldings().size()==1 ) {
				// update the portfolio holding
				this.updateThePortfolioHolding(portfolio.getPortfolioId(), portfolio.getHoldings().get(0), connection);
			}
			// if the updated does not have holding
			else if(oldPortfolio.getHoldings().size()==1 && portfolio.getHoldings().size()==0) {
				// delete the portfolio holding
				this.deleteThePortfolioHolding(portfolio.getPortfolioId(), instrumentId, connection);
			}
		}
		catch (SQLException e) {
			// log the error
			logger.error("Could not update client portfolio {}", e);
			// throw database exception
			throw new DatabaseException("Could not update client portfolios");
		}
		return null;
	}

	private List<Portfolio> mapTheResultSetTOlistPortfolios(ResultSet rs) throws SQLException {
		List<Portfolio> portfolios = new ArrayList<>();
		List<PortfolioHoldings> holdings = null;
		String currentPortfolio = "";
		while (rs.next()) {
			// p.portfolio_id
			String portId = rs.getString("portfolio_id");
			if (currentPortfolio.equals(portId)) {
				holdings.add(this.mapResultSetToPortfolioHolding(rs));
				
			} else {
				currentPortfolio=portId;
				holdings = new ArrayList<>();
				// p.name,
				String pName = rs.getString("name");
				// p.p_category,
				String category = rs.getString("p_category");
				// p.balance,
				BigDecimal balance = rs.getBigDecimal("balance");
				// p.client_id,
				BigInteger clientId = rs.getBigDecimal("client_id").toBigInteger();
				portfolios.add(new Portfolio(portId, clientId, category, balance, pName, holdings));
				rs.getString("instrument_id");
				// chceking if the portfolio has an instrument
				if (!rs.wasNull()) {
					holdings.add(this.mapResultSetToPortfolioHolding(rs));
				}
			}
		}
		return portfolios;
	}

	private PortfolioHoldings mapResultSetToPortfolioHolding(ResultSet rs) throws SQLException {
		// ph.instrument_id,
		String instrumentId = rs.getString("instrument_id");
		// ph.investment_price,
		BigDecimal investmentPrice = rs.getBigDecimal("investment_price");
		// ph.quantity ,
		BigInteger quantity = rs.getBigDecimal("quantity").toBigInteger();
		// ph.added_at ,
		LocalDateTime addedAt = rs.getTimestamp("added_at").toLocalDateTime();
		// ph.last_update_at
		LocalDateTime updateAt = rs.getTimestamp("added_at").toLocalDateTime();
		return new PortfolioHoldings(instrumentId, quantity, investmentPrice, updateAt, addedAt);
	}

	private void deleteThePortfolioHolding(String portfolioId, String instrumentId, Connection con)
			throws SQLException {
			String sql = " DELETE FROM portfolio_holding where PORTFOLIO_ID=? and INSTRUMENT_ID=?";
			try(PreparedStatement psmt = con.prepareStatement(sql)){
			psmt.clearParameters();
			psmt.setString(1, portfolioId);
			psmt.setString(2, instrumentId);
			int temp=psmt.executeUpdate();
			if(temp==0) {
				throw new DatabaseException("Could not delete client portfolio holding");
			}
		}
	}

	private void updateThePortfolioHolding(String portfolioId, PortfolioHoldings hold, Connection con)
			throws SQLException {
		String sql = "UPDATE portfolio_holding SET QUANTITY=?, INVESTMENT_PRICE=?, LAST_UPDATE_AT=? "
				+ " WHERE PORTFOLIO_ID=? and INSTRUMENT_ID=?";
		try(PreparedStatement psmt = con.prepareStatement(sql)){
			psmt.clearParameters();
			psmt.setBigDecimal(1, new BigDecimal(hold.getQuantity()));
			psmt.setBigDecimal(2, hold.getInvetsmentprice());
			psmt.setTimestamp(3, Timestamp.valueOf(hold.getLastUpdateAt()));
			psmt.setString(4, portfolioId);
			psmt.setString(5, hold.getInsrumentId());
	
			int temp=psmt.executeUpdate();
			
			if(temp==0) {
				throw new DatabaseException("Could not update portfolio holding");
			}
		}
	}

	private void addThePortfolioHolding(String portfolioId, PortfolioHoldings hold, Connection con)
			throws SQLException {
		String sql = " INSERT INTO PORTFOLIO_HOLDING(PORTFOLIO_ID,INSTRUMENT_ID,QUANTITY,INVESTMENT_PRICE,ADDED_AT, LAST_UPDATE_AT)"
				+ " VALUES(?,?,?,?,?,?)";
		try(PreparedStatement psmt = con.prepareStatement(sql)){
			psmt.clearParameters();
			psmt.setBigDecimal(3, new BigDecimal(hold.getQuantity()));
			psmt.setBigDecimal(4, hold.getInvetsmentprice());
			psmt.setTimestamp(5, Timestamp.valueOf(hold.getAddedAt()));
			psmt.setTimestamp(6, Timestamp.valueOf(hold.getLastUpdateAt()));
			psmt.setString(1, portfolioId);
			psmt.setString(2, hold.getInsrumentId());
			int temp=psmt.executeUpdate();
			if(temp==0) {
				throw new DatabaseException("Could not add client portfolio holding");
			}
		}
	}

	private void updateThePortfolio(Portfolio portfolio, Connection con) throws SQLException {
		// sql statement to update the portfolio balance and respective date
		String sql = """
				UPDATE PORTFOLIO SET
				    NAME=?,
				    BALANCE=?,
				    P_CATEGORY=?
				WHERE PORTFOLIO_ID=?
				""";
		// create a prepared statement
		try(PreparedStatement psmt = con.prepareStatement(sql)){
			// set the parameteters
			// NAME=?,
			psmt.setString(1, portfolio.getPortfolioName());
			// BALANCE=?,
			psmt.setBigDecimal(2, portfolio.getBalance());
			// P_CATEGORY=?
			psmt.setString(3, portfolio.getPortfolioTypeName());
			// PORTFOLIO_ID=?
			psmt.setString(4, portfolio.getPortfolioId());
			// execute the query
			int temp = psmt.executeUpdate();
			// if it caused 0 rows to be affected throw a new database exception
			if (temp == 0) {
				throw new DatabaseException("Could not update portfolio");
			}
		}
	}

}
