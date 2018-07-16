package com.drugstopper.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author rpsingh
 *
 */
@ServletComponentScan
@SpringBootApplication
public class ApplicationInit {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationInit.class);	

	public static void main(String[] args) {
		SpringApplication.run(ApplicationInit.class, args);
		logger.debug("--Application Started--");
	}
}
