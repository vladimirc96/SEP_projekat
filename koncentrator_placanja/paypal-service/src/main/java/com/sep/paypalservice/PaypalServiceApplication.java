package com.sep.paypalservice;

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

import javax.annotation.PostConstruct;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@SpringBootApplication
public class PaypalServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(PaypalServiceApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean corsFilterRegistration() {
		FilterRegistrationBean registrationBean =
				new FilterRegistrationBean(new CORSFilter());
		registrationBean.setName("CORS Filter");
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(1);
		return registrationBean;
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

		@Bean
		public RestTemplate getRestTemplate() {
			return new RestTemplate();
		}

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
