package com.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.pojo.Trade;
import com.repository.TradeRepository;

public class TradeService {
	
	@Autowired
	TradeRepository tradeRepository;
	
	public void add(Trade trade) {
		
	tradeRepository.save(trade);
	
	

	/*private Trade toEntity(Trade trade1) {
		// TODO Auto-generated method stub
		Trade entity=new Trade();
		entity=tradeRepository.save(trade1);
		retuen*/
	}
}
