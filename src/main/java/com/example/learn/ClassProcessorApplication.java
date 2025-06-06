package com.example.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.learn.repository")
public class ClassProcessorApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClassProcessorApplication.class, args);
	}
}