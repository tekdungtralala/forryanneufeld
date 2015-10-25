package com.upwork.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.upwork.job.Application;

@SpringBootApplication
public class TestApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
