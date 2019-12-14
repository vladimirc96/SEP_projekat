package com.sep.discoveryservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServiceApplication.class, args);
	}

	@Configuration
	public class SSLConfig {
		@Autowired
		private Environment env;

		@PostConstruct
		private void configureSSL() {
			//set to TLSv1.1 or TLSv1.2
			System.out.println("********** SSL-TRUST-STORE:" + env.getProperty("server.ssl.trust-store"));
			System.setProperty("https.protocols", "TLSv1.2");

			//load the 'javax.net.ssl.trustStore' and
			//'javax.net.ssl.trustStorePassword' from application.properties
			System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.trust-store"));
			System.setProperty("javax.net.ssl.trustStorePassword",env.getProperty("server.ssl.trust-store-password"));
		}
	}

}
