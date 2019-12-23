package com.sep.bank;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
@SpringBootApplication
public class BankApplication {

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
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

//	@Configuration
//	public class SSLConfig {
//		@Autowired
//		private Environment env;
//
//		@PostConstruct
//		private void configureSSL() {
//			//set to TLSv1.1 or TLSv1.2
//			System.setProperty("https.protocols", "TLSv1.2");
//
//			//load the 'javax.net.ssl.trustStore' and
//			//'javax.net.ssl.trustStorePassword' from application.properties
//			System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.trust-store"));
//			System.setProperty("javax.net.ssl.trustStorePassword",env.getProperty("server.ssl.trust-store-password"));
//		}
//	}


}


