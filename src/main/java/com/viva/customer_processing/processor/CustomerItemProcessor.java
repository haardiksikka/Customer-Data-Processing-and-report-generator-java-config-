package com.viva.customer_processing.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.viva.customer_processing.CustomerProcessingApplication;
import com.viva.customer_processing.entity.Customer;
import com.viva.customer_processing.listener.JobListner;
import com.viva.customer_processing.logger.Slf4jLogger;

public class CustomerItemProcessor implements ItemProcessor<Customer,Customer> {
	
	@Autowired
	public JobListner listener;
	
	Set<Customer> fileData = new HashSet<Customer>();
	Slf4jLogger logger = new Slf4jLogger(CustomerProcessingApplication.class);
	
	public Customer process(Customer customer) {
		List<Customer> dbRecords = listener.getDbRecords();
		if(dbRecords.contains(customer) || fileData.contains(customer)||customer.getPhoneNumber().length()==0) {
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
