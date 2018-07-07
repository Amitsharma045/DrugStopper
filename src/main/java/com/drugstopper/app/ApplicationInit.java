package com.drugstopper.app;

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
	public static void main(String[] args) {
		SpringApplication.run(ApplicationInit.class, args);
	}
}
