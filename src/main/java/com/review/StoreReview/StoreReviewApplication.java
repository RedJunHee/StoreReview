package com.review.StoreReview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@ComponentScan(basePackages={"com.review.StoreReview.config"})
@EnableJpaAuditing
@SpringBootApplication
public class StoreReviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreReviewApplication.class, args);
	}

}
