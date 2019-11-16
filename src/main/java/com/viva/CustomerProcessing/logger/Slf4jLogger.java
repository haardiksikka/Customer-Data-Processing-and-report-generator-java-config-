package com.viva.CustomerProcessing.logger;

import java.lang.reflect.Method;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;


public class Slf4jLogger {
	
	private Logger logger;
	private static final String SPACE = " ";
	private static String[] testMsisdn;

	
	public Slf4jLogger(final Class<?> className) {
		logger = LoggerFactory.getLogger(className);

	}
	
	public void info(final String statement) {
		logger.info(statement);
	}

}
