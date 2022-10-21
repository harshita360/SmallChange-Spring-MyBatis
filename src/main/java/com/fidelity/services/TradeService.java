package com.fidelity.services;

import com.fidelity.models.Order;
import com.fidelity.models.Trade;

public abstract class TradeService {
	public abstract Trade executeOrder(Order order) throws Exception;
}
