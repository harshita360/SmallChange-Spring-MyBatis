package com.fidelity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
//@ImportResource("classpath:beans.xml")
public class SmallchangeSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmallchangeSpringApplication.class, args);
	}
	
	@Bean
	@Scope("prototype")
	public Logger createLogger(InjectionPoint ip) {
	    Class<?> classThatWantsALogger = ip.getField().getDeclaringClass();
	    return LoggerFactory.getLogger(classThatWantsALogger);
	}

}
