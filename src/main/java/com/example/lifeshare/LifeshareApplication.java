package com.example.lifeshare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@ServletComponentScan
@SpringBootApplication
@MapperScan(basePackages = "com.example.lifeshare.api.**.mapper")
public class LifeshareApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifeshareApplication.class, args);
		System.out.println("HELLO~!!!");
	}

}
