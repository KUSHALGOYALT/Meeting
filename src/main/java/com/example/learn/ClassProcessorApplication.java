package com.example.learn;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.context.annotation.Configuration;
@SpringBootApplication
public class ClassProcessorApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClassProcessorApplication.class, args);
	}
}
@Configuration
class MongoConfig extends AbstractMongoClientConfiguration {
	@Override
	protected String getDatabaseName() {
		return "learning_assistant_db";
	}
}