package com.repository;

import java.util.Calendar;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pojo.Market;

public interface MarketRepository extends JpaRepository<Market, Calendar>{

	
}
