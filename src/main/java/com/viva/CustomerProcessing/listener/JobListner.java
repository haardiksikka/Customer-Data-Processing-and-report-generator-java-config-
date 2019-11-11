package com.viva.CustomerProcessing.listener;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import com.viva.CustomerProcessing.entity.Customer;


@Component
public class JobListner extends JobExecutionListenerSupport {
	@Autowired
	private final JdbcTemplate jdbcTemplate;
	@Autowired DataSource dataSource;
//	private static final Logger log = LoggerFactory.getLogger(JobListner.class);
	
	public List<Customer> dbRecords;
	
	@Autowired
	public JobListner(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		dbRecords = new ArrayList<Customer>();
	}
	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		this.jdbcTemplate.setDataSource(dataSource);
	//	List<Customer> success = new ArrayList<Customer>();
		jdbcTemplate.query("SELECT first_name, middle_name,last_name,address, city, phone_number FROM customer",
				(rs, row) -> new Customer(
					rs.getString(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(6),
					rs.getString(5),
					rs.getString(4)
					)).forEach(customer -> dbRecords.add(customer));
		
	}
}
