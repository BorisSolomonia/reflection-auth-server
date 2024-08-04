package com.boris.auth_server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDiscoveryClient
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner dataLoader(JdbcTemplate jdbcTemplate) {
//		return args -> {
//			// Create default clients and other setup here
//		};
//	}
}
