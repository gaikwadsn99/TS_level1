package com.pojo;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="market")
public class Market {
	public Market(int i, Calendar tradeExecutionTime, double d, double e, double f, double g, double h, double j,
			double k, double l, double m, double n, double o, double p) {
		// TODO Auto-generated constructor stub
	}
	public Market() {
		this.tradeTime = Calendar.getInstance();
	}
	
	@Id
	int marketId;
	@Column
	Calendar tradeTime;
	

	double AppleES;
	double ApplePut;
	double AppleCall;
	double AppleFut;
	double FacebookES;
	double FacebookPut;
	double FacebookCall;
	double FacebookFut;
	double WalmartES;
	double WalmartPut;
	double WalmartCall;
	double WalmartFut;
	public int getMarketId() {
		return marketId;
	}
	public void setMarketId(int marketId) {
		this.marketId = marketId;
	}
	@Override
	public String toString() {
		return "Market [tradeTime=" + tradeTime + ", AppleES=" + AppleES + ", ApplePut=" + ApplePut + ", AppleCall="
				+ AppleCall + ", AppleFut=" + AppleFut + ", FacebookES=" + FacebookES + ", FacebookPut=" + FacebookPut
				+ ", FacebookCall=" + FacebookCall + ", FacebookFut=" + FacebookFut + ", WalmartES=" + WalmartES
				+ ", WalmartPut=" + WalmartPut + ", WalmartCall=" + WalmartCall + ", WalmartFut=" + WalmartFut + "]";
	}
	public void update( double d, double e, double f, double g, double h, double j,
			double k, double l, double m, double n, double o, double p) {
		this.AppleES = d;
		this.ApplePut=  e;
		this.AppleCall= f;
		this.AppleFut= g;
		this.FacebookES= h;
		this.FacebookPut= j;
		this.FacebookCall= k;
		this.FacebookFut= l; this.WalmartES= m;this.WalmartPut= n;this.WalmartCall= o;
		this.WalmartFut= p;
	}
	

	public Calendar getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(Calendar tradeTime) {
		this.tradeTime = tradeTime;
	}
	public double getAppleES() {
		return AppleES;
	}
	public void setAppleES(double appleES) {
		AppleES = appleES;
	}
	public double getApplePut() {
		return ApplePut;
	}
	public void setApplePut(double applePut) {
		ApplePut = applePut;
	}
	public double getAppleCall() {
		return AppleCall;
	}
	public void setAppleCall(double appleCall) {
		AppleCall = appleCall;
	}
	public double getAppleFut() {
		return AppleFut;
	}
	public void setAppleFut(double appleFut) {
		AppleFut = appleFut;
	}
	public double getFacebookES() {
		return FacebookES;
	}
	public void setFacebookES(double facebookES) {
		FacebookES = facebookES;
	}
	public double getFacebookPut() {
		return FacebookPut;
	}
	public void setFacebookPut(double facebookPut) {
		FacebookPut = facebookPut;
	}
	public double getFacebookCall() {
		return FacebookCall;
	}
	public void setFacebookCall(double facebookCall) {
		FacebookCall = facebookCall;
	}
	public double getFacebookFut() {
		return FacebookFut;
	}
	public void setFacebookFut(double facebookFut) {
		FacebookFut = facebookFut;
	}
	public double getWalmartES() {
		return WalmartES;
	}
	public void setWalmartES(double walmartES) {
		WalmartES = walmartES;
	}
	public double getWalmartPut() {
		return WalmartPut;
	}
	public void setWalmartPut(double walmartPut) {
		WalmartPut = walmartPut;
	}
	public double getWalmartCall() {
		return WalmartCall;
	}
	public void setWalmartCall(double walmartCall) {
		WalmartCall = walmartCall;
	}
	public double getWalmartFut() {
		return WalmartFut;
	}
	public void setWalmartFut(double walmartFut) {
		WalmartFut = walmartFut;
	}
}