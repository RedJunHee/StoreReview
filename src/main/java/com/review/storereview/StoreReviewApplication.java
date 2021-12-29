package com.review.storereview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@ComponentScan(basePackages={"com.review.storereview.config"})
@EnableJpaAuditing
@SpringBootApplication
public class StoreReviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreReviewApplication.class, args);
	}

}
