package com.viva.customer_processing.entity;

import java.util.Date;

public class FeeInfo {
	private Double feeAmount;
	
	private String phoneNumber;
	private Date feeDate;
	
	public FeeInfo() {
         
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

	public Date getFeeDate() {
		return feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}

	
	
	
	
}
