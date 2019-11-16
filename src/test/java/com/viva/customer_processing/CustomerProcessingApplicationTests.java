package com.viva.customer_processing;



import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import javax.batch.runtime.BatchStatus;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.viva.customer_processing.CustomerProcessingApplication;
import com.viva.customer_processing.configuration.BatchConfiguration;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;

import junit.framework.Assert;

@SpringBatchTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CustomerProcessingApplication.class)
@SpringBootTest
class CustomerProcessingApplicationTests {
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;
	
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
	
	
	
	@Test
	void contextLoads() throws Exception {
		System.out.println("sknckldsc");
			
			JobExecution jobExecution;			
				jobExecution = jobLauncherTestUtils.launchJob();
		        Assert.assertEquals(BatchStatus.COMPLETED.toString(), jobExecution.getStatus().toString());
		
	}

}
