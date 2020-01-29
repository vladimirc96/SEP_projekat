package com.sep.bank;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BankApplication {



	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}
//
//	@Bean
//	public FilterRegistrationBean corsFilterRegistration() {
//		FilterRegistrationBean registrationBean =
//				new FilterRegistrationBean(new CORSFilter());
//		registrationBean.setName("CORS Filter");
//		registrationBean.addUrlPatterns("/*");
//		registrationBean.setOrder(1);
//		return registrationBean;
//	}

	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
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
			//set to TLSv1.1 or TLSv1.2
			System.setProperty("https.protocols", "TLSv1.2");

			//load the 'javax.net.ssl.trustStore' and
			//'javax.net.ssl.trustStorePassword' from application.properties
			System.setProperty("javax.net.ssl.trustStore", env.getProperty("server.ssl.trust-store"));
			System.setProperty("javax.net.ssl.trustStorePassword",env.getProperty("server.ssl.trust-store-password"));
		}
	}


}


