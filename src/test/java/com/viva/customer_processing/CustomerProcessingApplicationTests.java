package com.viva.customer_processing;



import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import javax.batch.runtime.BatchStatus;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.viva.customer_processing.CustomerProcessingApplication;
import com.viva.customer_processing.configuration.BatchConfiguration;
import com.viva.customer_processing.listener.JobListner;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;

import junit.framework.Assert;

@SpringBatchTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=BatchConfiguration.class)
@SpringBootTest(classes=CustomerProcessingApplication.class)
class CustomerProcessingApplicationTests {
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("importUserJob")
	private Job job;
	
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
	
	@Autowired
    private BatchConfiguration config;
	
	@Autowired
	private JobRepository jobRepository;
	

	@Test
	void job_test() throws Exception {					
				JobExecution jobExecution = jobLauncherTestUtils.launchJob();					
		        Assert.assertEquals(BatchStatus.COMPLETED.toString(), jobExecution.getStatus().toString());				
	}
	
	@Test
	void test_customer_step() {		
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("customerStep");
		Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode().toString());
	}
	@Test
	void test_fee_info_step() {
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("feeInfoStep");
		Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode().toString());
	}
	
	@Test
	void test_report_generator_step() {
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("reportGeneratorStep");
		Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode().toString());
	}
	
//	@Test
//	void test_deactivate_customer_step() {
//		JobExecution jobExecution = jobLauncherTestUtils.launchStep("deactivateCustomer");
//		Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode().toString());
//	}
//	
//	@Test
//	void test_delete_fee_info_step() {
//		JobExecution jobExecution = jobLauncherTestUtils.launchStep("deleteFeeInfo");
//		Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode().toString());
//	}
	
	
	

}
