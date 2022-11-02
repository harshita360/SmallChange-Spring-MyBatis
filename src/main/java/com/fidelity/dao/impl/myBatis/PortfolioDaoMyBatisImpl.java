package com.fidelity.dao.impl.myBatis;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.dao.PortfolioDao;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.mappers.PortfolioMapper;
import com.fidelity.models.Portfolio;

@Component()
@Profile("my-batis")
public class PortfolioDaoMyBatisImpl extends PortfolioDao {

	// private final Logger logger = LoggerFactory.getLogger(PortfolioDao.class);

	@Autowired
	private Logger logger;

	@Autowired
	PortfolioMapper mapper;

	@Override
	@Transactional
	public List<Portfolio> getPortfoliosForAUser(BigInteger clientId) {
		// List<Portfolio> portfolios=null;
		logger.debug("Getting the all portfolios of client");
		return mapper.getPortfoliosOfClient(clientId);
	}

	@Override
	@Transactional
	public Portfolio getPortfolioForAuserFromPortfolioId(String portfolioId) {
		logger.debug("Getting portfolio from its id");
		return mapper.getPortfolioFromPortfolioId(portfolioId);
	}

	@Override
	@Transactional
	public Portfolio addNewPortfolio(Portfolio portfolio) {
		logger.debug("Add New Portfolio");
		int temp = mapper.addNewPortfolio(portfolio);

		// if the new row isn not inserted, then throw a new dataabse exception
		if (temp == 0) {
			throw new DatabaseException("Could not add client portfolio");
		}

		return portfolio;
	}

	@Override
	@Transactional
	public void deletePortfolioById(String portfolioID) {
		// Sql statement to delete portfolio from its ID
		logger.debug("Deleting portfolio from its id");
		int temp = mapper.deletePortfolioByPortfolioId(portfolioID);
		// if the number of reflected rows is 0, then throwexception
		if (temp == 0) {
			throw new DatabaseException("Could not delete all user portfolios");
		}

	}

	@Override
	@Transactional
	public void deletePortfolioByClientId(BigInteger clientId) {
		// execute the update
		logger.debug("Delete all clients portfolios");
		int temp = mapper.deletePortfoliosOfClient(clientId);
		// if the number of reflected rows is 0, then throwexception
		if (temp == 0) {
			throw new DatabaseException("Could not delete all user portfolios");
		}
	}

	@Override
	@Transactional
	public Portfolio getPortfolioFromIdAndLoadOfInstrument(String portfolioId, String instrumentId) {
		logger.debug("Get the portfolio fromits id of instrument only");
		Map<Object, Object> data = new HashMap<>();
		data.put("instrumentId", instrumentId);
		data.put("portfolioId", portfolioId);
		return mapper.getPortfolioFromPortfolioIdOfInstrument(data);
	}

	@Override
	@Transactional
	public Portfolio updatePortfolioFromIdAndLoadOfInstrument(Portfolio portfolio, String instrumentId) {
		logger.debug("Update the portfolio fromits id of instrument only");
		// get the current portfolio of the given id from the database
		Map<Object, Object> data = new HashMap<>();
		data.put("instrumentId", instrumentId);
		data.put("portfolioId", portfolio.getPortfolioId());
		Portfolio oldPortfolio = mapper.getPortfolioFromPortfolioIdOfInstrument(data);

		if (oldPortfolio == null) {
			throw new DatabaseException("Portfolio does not exist");
		}
		// update the portfolio details
		int rowsUpdated = this.mapper.updatePortfolio(portfolio);
		if (rowsUpdated != 1) {
			throw new DatabaseException("Could not update portfolio");
		}
		int holdRowsUpdated = 0;
		Map<Object, Object> holdingsDataToUpdate = new HashMap<>();

		holdingsDataToUpdate.put("portfolioId", portfolio.getPortfolioId());

		// if the new pprtfolio has an has new holding
		if (oldPortfolio.getHoldings().size() == 0 && portfolio.getHoldings().size() == 1) {
			// add the instrument holding
			holdingsDataToUpdate.put("holding", portfolio.getHoldings().get(0));
			holdRowsUpdated = mapper.addPortfolioHolding(holdingsDataToUpdate);

		}
		// if the retrived and updated data has a single portfolio holding of thst
		// instrument
		else if (oldPortfolio.getHoldings().size() == 1 && portfolio.getHoldings().size() == 1) {
			// update the portfolio holding
			holdingsDataToUpdate.put("holding", portfolio.getHoldings().get(0));
			holdRowsUpdated = mapper.updatePortfolioHolding(holdingsDataToUpdate);
		}
		// if the updated does not have holding
		else if (oldPortfolio.getHoldings().size() == 1 && portfolio.getHoldings().size() == 0) {
			// delete the portfolio holding

			holdingsDataToUpdate.put("instrumentId", oldPortfolio.getHoldings().get(0).getInsrumentId());

			holdRowsUpdated = mapper.deletePortfolioHolding(holdingsDataToUpdate);
		}
		if (holdRowsUpdated == 0) {
			throw new DatabaseException("Holdings update un succcessful");
		}
		return null;
	}

}
