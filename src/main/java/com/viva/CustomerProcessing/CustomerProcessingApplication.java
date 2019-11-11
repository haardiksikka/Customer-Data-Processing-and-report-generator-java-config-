package com.viva.CustomerProcessing;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.viva.CustomerProcessing.logger.Slf4jLogger;


@SpringBootApplication
public class CustomerProcessingApplication {
	//static Slf4jLogger logger = new Slf4jLogger(CustomerProcessingApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(CustomerProcessingApplication.class, args);
	//	logger.info("---------------Hello------------------");
	}
	

}
