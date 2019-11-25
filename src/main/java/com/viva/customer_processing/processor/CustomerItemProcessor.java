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
import com.viva.customer_processing.utils.MobileNumberValidator;

public class CustomerItemProcessor implements ItemProcessor<Customer,Customer> {
	
	@Autowired
	public JobListner listener;
	
	Set<Customer> fileData = new HashSet<Customer>();
	Slf4jLogger logger = new Slf4jLogger(CustomerProcessingApplication.class);
	
	public Customer process(Customer customer) {
		Set<Customer> dbRecords = listener.getDbRecords();
		if(dbRecords.contains(customer) || fileData.contains(customer)) {
			logger.error("Customer with phone number "+customer.getPhoneNumber()+" ------->Registration Failed -->Duplicate phone number");		
			return null;
		}
		else if(!MobileNumberValidator.isValid(customer.getPhoneNumber())) {
			System.out.println(customer.getFirstName());
			logger.error("Customer with phone number "+customer.getPhoneNumber()+" ------->Registration Failed : Phone Number should be numeric containing digits [0-9].");
			return null;
		}
		else if(customer.getPhoneNumber().length()==0) {
			logger.error("Customer with phone number "+customer.getPhoneNumber()+" ------->Registration Failed -->Phone number field is empty.");
			return null;
		}
		else if(customer.getFirstName().length()==0) {
			logger.info("Customer with phone number "+customer.getPhoneNumber()+" ------->Registration Failed -->First Name field is empty.");
			return null;
		}
		else if(customer.getAddress().length()==0) {
			logger.info("Customer with phone number "+customer.getAddress()+" ------->Registration Failed -->Address field is empty.");
			return null;
		}
		else if(customer.getCity().length()==0) {
			logger.info("Customer with phone number "+customer.getCity()+" ------->Registration Failed -->City field is empty.");
			return null;
		}
		else {
			fileData.add(customer);
			logger.info("Customer with phone number "+customer.getPhoneNumber()+" ------->Registration Status : Success.");
			return customer;
		}
		
	}

}
