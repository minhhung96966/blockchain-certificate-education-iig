package com.minhhung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.minhhung"})
//@EnableJpaRepositories(basePackages= "com.minhhung.repository")
@EnableTransactionManagement
@EnableMongoRepositories(basePackages={"com.minhhung.persistence"})
@EntityScan(basePackages="com.minhhung.model")
public class CertificateEducationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CertificateEducationApplication.class, args);
	}
}
