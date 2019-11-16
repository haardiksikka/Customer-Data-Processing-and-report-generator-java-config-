package com.viva.CustomerProcessing.processor;


import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.viva.CustomerProcessing.entity.Customer;
import com.viva.CustomerProcessing.entity.FeeInfo;
import com.viva.CustomerProcessing.listener.JobListner;

public class FeeInfoProcessor implements ItemProcessor<Customer, FeeInfo> {
	
	@Autowired
	public JobListner job;
	
	@Value("${customer.feeAmount}")
	double feeAmount;
	
	
	public FeeInfo process(Customer customer) {
		if(!job.dbRecords.contains(customer)) {
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

