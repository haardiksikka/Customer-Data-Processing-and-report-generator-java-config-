package com.viva.customer_processing;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.viva.customer_processing.logger.Slf4jLogger;


@SpringBootApplication
public class CustomerProcessingApplication {
	public static void main(String[] args) {
		SpringApplication.run(CustomerProcessingApplication.class, args);	
	}
	

}
