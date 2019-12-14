package com.sep.bankservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.net.URLClassLoader;

@EnableEurekaClient
@SpringBootApplication
public class BankServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankServiceApplication.class, args);
	}

	@Configuration
	public class SSLConfig {
		@Bean
		public RestTemplate getRestTemplate() {
			return new RestTemplate();
		}

		@Autowired
		private Environment env;

		@PostConstruct
		private void configureSSL() {
			System.setProperty("https.protocols", "TLSv1.2");

			//load the 'javax.net.ssl.trustStore' and
			//'javax.net.ssl.trustStorePassword' from application.properties
			System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.trust-store"));
			System.setProperty("javax.net.ssl.trustStorePassword",env.getProperty("server.ssl.trust-store-password"));
		}
	}

}
