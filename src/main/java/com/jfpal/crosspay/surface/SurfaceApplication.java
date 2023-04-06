package com.jfpal.crosspay.surface;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SurfaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurfaceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
	    
	    return new RestTemplate();
		
	}
}
