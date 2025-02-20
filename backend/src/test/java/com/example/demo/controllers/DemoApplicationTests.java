package com.example.demo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootTest
@EnableJpaRepositories(basePackages = "com.example.repositories")
@EntityScan("com.example.demo.models")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}