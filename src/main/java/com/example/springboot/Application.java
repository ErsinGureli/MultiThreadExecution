package com.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import service.ThreadExecuterService;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
		ThreadExecuterService threadExecuterService = new ThreadExecuterService();
		threadExecuterService.executeTask();
	}
}
