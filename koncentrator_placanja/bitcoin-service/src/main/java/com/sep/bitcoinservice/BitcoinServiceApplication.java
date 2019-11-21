package com.sep.bitcoinservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class BitcoinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitcoinServiceApplication.class, args);
	}

}
