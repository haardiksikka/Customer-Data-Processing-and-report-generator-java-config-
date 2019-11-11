package com.viva.CustomerProcessing.entity;

import java.util.Date;

public class FeeInfo {
	private Double feeAmount;
	
	private String phoneNumber;
	private Date createdOn;
	
	public FeeInfo() {
         
	}

	public FeeInfo(Double feeAmount, String phoneNumber, Date createdOn) {
		super();
		this.feeAmount = feeAmount;
		this.phoneNumber = phoneNumber;
		this.createdOn = createdOn;
	}

	public Double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	
	
	
}
