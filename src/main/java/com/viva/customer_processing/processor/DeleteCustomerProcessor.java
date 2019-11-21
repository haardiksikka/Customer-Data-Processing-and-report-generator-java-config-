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

public class DeleteCustomerProcessor implements ItemProcessor<Customer, Customer> {
	
	@Autowired
	public JobListner listener;
	Set<Customer> fileData = new HashSet<Customer>();
	@Value("${customer.feeAmount}")
	double feeAmount;
	
	
	public Customer process(Customer customer) {
		//System.out.println(customer.getPhoneNumber());
		return customer;
	}

}


