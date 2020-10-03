package com.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


//@Entity(name = "marketlist")
public class MarketList {

	//@Id
	private int securityId;
	//@Column

	double[]price = {0,0,0,0};  // 0-Es, 1-Put 2-Call 3-Fut
	public MarketList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getSecurityId() {
		return securityId;
	}
	public void setSecurityId(int securityId) {
		this.securityId = securityId;
	}
	public double[] getPrice() {
		return price;
	}
	public void setPrice(double[] price) {
		this.price = price;
	}
	public MarketList(int securityId, double[] price) {
		super();
		this.securityId = securityId;
		this.price = price;
	}

	

	

}
