package com.sep.bitcoinservice;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.annotation.PostConstruct;

@EnableEurekaClient
@SpringBootApplication
public class BitcoinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitcoinServiceApplication.class, args);
	}

	@Bean
	HttpComponentsClientHttpRequestFactory requestFactory() {
		CloseableHttpClient httpClient
				= HttpClients.custom()
				.setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory
				= new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		return requestFactory;
	}


	@Configuration
	public class SSLConfig {
		@Autowired
		private Environment env;

		@PostConstruct
		private void configureSSL() {

			//set to TLSv1.1 or TLSv1.2
			System.setProperty("https.protocols", "TLSv1.2");

			//load the 'javax.net.ssl.trustStore' and
			//'javax.net.ssl.trustStorePassword' from application.properties
			System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.trust-store"));
			System.setProperty("javax.net.ssl.trustStorePassword",env.getProperty("server.ssl.trust-store-password"));
		}
	}

}
