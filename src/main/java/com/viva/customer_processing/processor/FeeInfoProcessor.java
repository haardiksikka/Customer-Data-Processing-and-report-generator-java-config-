package com.viva.customer_processing.processor;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viva.customer_processing.entity.Customer;
import com.viva.customer_processing.entity.FeeInfo;
import com.viva.customer_processing.listener.JobListner;

public class FeeInfoProcessor implements ItemProcessor<Customer, FeeInfo> {
	
	@Autowired
	public JobListner listener;
	Set<Customer> fileData = new HashSet<Customer>();
	@Value("${customer.feeAmount}")
	double feeAmount;
	
	
	public FeeInfo process(Customer customer) {
		List<Customer> dbRecords = listener.getDbRecords();
		if(dbRecords.contains(customer) || fileData.contains(customer)) {
		
		return null;
		}
		else {
			FeeInfo feeDetails = new FeeInfo();
			feeDetails.setPhoneNumber(customer.getPhoneNumber());
			feeDetails.setFeeAmount(feeAmount);
			return feeDetails;
			
		}
	}

}

