package com.viva.customer_processing.processor;


import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viva.customer_processing.entity.Customer;
import com.viva.customer_processing.entity.FeeInfo;
import com.viva.customer_processing.listener.JobListner;

public class FeeInfoProcessor implements ItemProcessor<Customer, FeeInfo> {
	
	@Autowired
	public JobListner listener;
	
	@Value("${customer.feeAmount}")
	double feeAmount;
	
	
	public FeeInfo process(Customer customer) {
		List<Customer> dbRecords = listener.getDbRecords();
		if(!dbRecords.contains(customer)) {
		FeeInfo feeDetails = new FeeInfo();
		feeDetails.setPhoneNumber(customer.getPhoneNumber());
		feeDetails.setFeeAmount(feeAmount);
		return feeDetails;
		}
		else {
			return null;
		}
	}

}

