package com.viva.CustomerProcessing.processor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.viva.CustomerProcessing.CustomerProcessingApplication;
import com.viva.CustomerProcessing.entity.Customer;
import com.viva.CustomerProcessing.listener.JobListner;
import com.viva.CustomerProcessing.logger.Slf4jLogger;

public class CustomerItemProcessor implements ItemProcessor<Customer,Customer> {
	
	@Autowired
	public JobListner job;
	
	Set<Customer> fileData = new HashSet<Customer>();
	Slf4jLogger logger = new Slf4jLogger(CustomerProcessingApplication.class);
	
	public Customer process(Customer customer) {
	
		if(job.dbRecords.contains(customer) || fileData.contains(customer)) {
			logger.info("Customer with phone number "+customer.getPhoneNumber()+" ------->Registration Status : Failed.");
			return null;
		}
		else {
			fileData.add(customer);
			logger.info("Customer with phone number "+customer.getPhoneNumber()+" ------->Registration Status : Success.");
			return customer;
		}
		
	}

}
