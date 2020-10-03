package com.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pojo.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer>{
	
	//@Query(value= "INSERT INTO trade (tradeId,tradeExecutionTime,brokerName,securityId, security, securityName, tradeType, marketPrice, price, isChecked, quantity,customerId) VALUES(trade[0],trade[1],trade[2],trade[3],trade[4],trade[5],trade[6],trade[7],trade[8],trade[9],trade[10],trade[11])")
	//public Void addTrade(Trade trade);
	@Transactional
	@Modifying
	@Query("update trade set is_checked = true where trade_id  = ?1")
	void setIsChecked( Integer tradeId);
	
	public Void findByIsChecked(Boolean boolean1);
	
	@Transactional
	@Modifying
	@Query(
			  value = "SELECT * FROM trade ORDER BY trade_execution_time", 
			  countQuery = "SELECT count(*) FROM trade", 
			  nativeQuery = true) 
	public List<Trade> orderByTime();
	
	
}
