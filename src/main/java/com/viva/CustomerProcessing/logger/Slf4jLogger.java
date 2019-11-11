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

	public Slf4jLogger(String aStrVal) {
		logger = LoggerFactory.getLogger(aStrVal);
	}

	/**
	 * This method is used to reload the logger properties.
	 */
	public void reloadLogBackupConfiguration() throws JoranException {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		ContextInitializer ci = new ContextInitializer(loggerContext);
		URL url = ci.findURLOfDefaultConfigurationFile(true);
		loggerContext.reset();
		ci.configureByResource(url);
	}

	/**
	 * Method to load test Msisdn
	 */
	public void loadTestMsisdn(String[] msisdn) {
		testMsisdn = msisdn;
	}

	/**
	 * Method to check msisdn belongs to test MSISDN
	 */

	private boolean checkMsisdn(String msisdn) {
		for (int i = 0; i < testMsisdn.length; i++)
			if (msisdn.equals(testMsisdn[i]))
				return true;
		return false;
	}

	/**
	 * Checks if is debug enabled.
	 *
	 * @return If Debug is enabled
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * Checks if is info enabled.
	 *
	 * @return If Info is enabled
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	/**
	 * This method logs the statement with the DEBUG level (It designates
	 * fine-grained informational events that are most useful to debug an
	 * application). If this category is DEBUG enabled, then it proceeds to call
	 * all the registered appenders in this category and also higher in the
	 * hierarchy depending on the value of the additivity flag.
	 * 
	 * @param statement
	 *            the statement to be logged
	 */
	public void debug(final String statement) {

		if (!isDebugEnabled() && MDC.get("msisdn") != null && checkMsisdn(MDC.get("msisdn")))
			logger.info(statement);
		else
			logger.debug(statement);

	}

	public void debug(final String statement, final Exception e) {
		if (!isDebugEnabled() && MDC.get("msisdn") != null && checkMsisdn(MDC.get("msisdn")))
			logger.info(append(statement, e));
		else
			logger.debug(append(statement, e));
	}

	
	public void info(final String statement) {
		logger.info(statement);
	}

	public void info(final String statement, final Exception e) {
		logger.info(append(statement, e));
	}

	
	public void warn(final String statement) {
		logger.warn(statement);
	}

	
	public void warn(final String statement, final Exception e) {
		logger.warn(append(statement, e));
	}

	
	public void error(final String statement) {
		logger.error(statement);
	}

	public void error(final String statement, final Exception e) {
		logger.error(append(statement, e));
	}

	public void fatal(final String statement) {
		logger.error(statement);
	}

	public void fatal(final String statement, final Exception e) {
		logger.error(append(statement, e));
	}

	private String append(final String statement, final Exception e) {
		StringBuilder sb = new StringBuilder();
		sb.append(statement);
		sb.append(SPACE);
		sb.append(getExceptionDetails(e));
		return sb.toString();
	}

	private String getExceptionDetails(Exception e) {
		StringBuilder st = new StringBuilder();
		if (e != null) {
			StackTraceElement[] traces = e.getStackTrace();
			int len = traces.length;
			if (traces != null && len > 0) {
				if (len > 3 && logger.isInfoEnabled()) {
					len = 3;
				}
				st.append("EXP: " + e + ": " + e.getMessage());
				for (int i = 0; i < len; i++) {
					StackTraceElement ste = traces[i];
					if (ste != null) {
						String s = ste.getClassName();
						if (s != null) {
							st.append("[Class: " + s + "]");
						}

						s = ste.getMethodName();
						if (s != null) {
							st.append("[Method: " + s + "]");
						}
						st.append("[Line: " + ste.getLineNumber() + "]");
					}
				}
			}
		}
		return st.toString();
	}

}
