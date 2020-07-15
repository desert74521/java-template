package com.test.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	 
	@Bean
    public RestTemplate restTemplate(){
		 SimpleClientHttpRequestFactory requestFactory = new   SimpleClientHttpRequestFactory();
		    requestFactory.setConnectTimeout(500000);
		    requestFactory.setReadTimeout(500000);

		    RestTemplate restTemplate = new RestTemplate(requestFactory);
		    return restTemplate;
    }

}
