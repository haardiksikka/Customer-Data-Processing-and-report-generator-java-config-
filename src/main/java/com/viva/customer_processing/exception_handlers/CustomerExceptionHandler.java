package com.viva.customer_processing.exception_handlers;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

import com.viva.customer_processing.CustomerProcessingApplication;
import com.viva.customer_processing.logger.Slf4jLogger;

public class CustomerExceptionHandler implements SkipPolicy {
	
	Slf4jLogger logger = new Slf4jLogger(CustomerProcessingApplication.class);
	
	@Override
    public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
        if (exception instanceof Exception && skipCount <= 10000) {
        	logger.error("Customer -------> Registration Failed : Invalid Input Data");		
            return true;
        } else {
            return false;
        }
    }
}
