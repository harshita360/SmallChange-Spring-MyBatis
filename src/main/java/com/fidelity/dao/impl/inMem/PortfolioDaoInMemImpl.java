package com.fidelity.dao.impl.inMem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fidelity.dao.PortfolioDao;
import com.fidelity.exceptions.DatabaseException;
import com.fidelity.models.Portfolio;
import com.fidelity.models.PortfolioHoldings;

@Component("portfolioDaoInMemory")
public class PortfolioDaoInMemImpl extends PortfolioDao {
	
	
	List<Portfolio> portfolios;
	
	public PortfolioDaoInMemImpl() {
		this.portfolios=new ArrayList<>();
	}

	@Override
	public List<Portfolio> getPortfoliosForAUser(BigInteger clientId) {
		return this.portfolios.stream().filter( p -> p.getClientId().equals(clientId)).toList();
	}

	@Override
	public Portfolio getPortfolioForAuserFromPortfolioId(String portfolioId) {
		List<Portfolio> filtered= this.portfolios.stream().filter( p-> p.getPortfolioId().equals(portfolioId)).toList();
		if(filtered.size()==1) {
			return filtered.get(0);
		}
		return null;
	}

	@Override
	public Portfolio addNewPortfolio(Portfolio portfolio) {
		this.portfolios.add(portfolio);
		return portfolio;
	}

	@Override
	public void deletePortfolioById(String portfolioID) {
		Portfolio portfolio=this.getPortfolioForAuserFromPortfolioId(portfolioID);
		if(portfolio==null) {
			throw new DatabaseException("Portfolio Not Present");
		}
		this.portfolios.remove(portfolio);
		
	}

	@Override
	public Portfolio getPortfolioFromIdAndLoadOfInstrument(String portfolioId, String instrumentId) {
		Portfolio po=this.getPortfolioForAuserFromPortfolioId(portfolioId);
		PortfolioHoldings hold=po.getTheHoldingsByInstrumnentId(instrumentId);
		List<PortfolioHoldings> holdings=new ArrayList<>();
		if(hold!=null) {
			holdings.add(hold);
		}
		Portfolio returned=new Portfolio(po.getPortfolioId(),po.getClientId(),po.getPortfolioTypeName(),
				po.getBalance(),po.getPortfolioName(),holdings);
		return returned;
		
	}

	@Override
	public void deletePortfolioByClientId(BigInteger clientId) {
		this.portfolios.removeAll(this.getPortfoliosForAUser(clientId));
		
	}

	@Override
	public Portfolio updatePortfolioFromIdAndLoadOfInstrument(Portfolio portfolio,String instrumentId) {
		Portfolio existing=this.getPortfolioForAuserFromPortfolioId(portfolio.getPortfolioId());
		List<PortfolioHoldings> filtered=existing.getHoldings().stream().filter( h -> h.getInsrumentId().equals(instrumentId)).toList();
		// if the user is having one, then do as below
		if(filtered.size()==1) {
			// if the updated portfolio has a entry, then do as below
			if(portfolio.getHoldings().size()==1) {
				PortfolioHoldings updatedValue=portfolio.getHoldings().get(0);
				filtered.get(0).setQuantity(updatedValue.getQuantity());
				filtered.get(0).setInvetsmentprice(updatedValue.getInvetsmentprice());
				filtered.get(0).setLastUpdateAt(updatedValue.getLastUpdateAt());
			}else {
				// if the user has sold all his instruments there is a need to remove that entry
				existing.getHoldings().remove(filtered.get(0));
			}
			
		}else {
			PortfolioHoldings updatedValue=portfolio.getHoldings().get(0);
			existing.getHoldings().add(updatedValue);
		}
		existing.setBalance(portfolio.getBalance());
		return existing;
	}

}
