package com.viva.CustomerProcessing.entity;

import java.util.Date;

public class FeeInfo {
	private Double feeAmount;
	
	private String phoneNumber;
	private Date feeDate;
	
	public FeeInfo() {
         
	}

	public FeeInfo(Double feeAmount, String phoneNumber, Date feeDate) {
		super();
		this.feeAmount = feeAmount;
		this.phoneNumber = phoneNumber;
		this.feeDate = feeDate;
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
		return feeDate;
	}

	public void setCreatedOn(Date createdOn) {
		this.feeDate = createdOn;
	}
	
	
	
	
}
