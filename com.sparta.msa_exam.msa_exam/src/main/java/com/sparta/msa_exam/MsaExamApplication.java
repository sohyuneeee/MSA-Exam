package com.sparta.msa_exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MsaExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsaExamApplication.class, args);
	}

}
