package com.sep.sellers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SellersApplication {

	public static void main(String[] args) {
		SpringApplication.run(SellersApplication.class, args);
	}

}
