package com.review.StoreReview.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.review.StoreReview.repository"},
        entityManagerFactoryRef = "CUSTEntityManagerFactory",
        transactionManagerRef = "CUSTTransactionManager"
)
public class CUSTJpaConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.cust-datasource")
    public DataSource CUSTDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean CUSTEntityManagerFactory(EntityManagerFactoryBuilder builder, // Could not autowire. No beans of 'EntityManagerFactoryBuilder' type found.
                                                                           @Qualifier("CUSTDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.review.StoreReview.domain.CUST")
                .persistenceUnit("CUSTEntityManager")
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager CUSTTransactionManager(@Qualifier("CUSTEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    // 참고 : https://lemontia.tistory.com/967
    // 참고2 : https://suho413.tistory.com/entry/Spring-Boot-%EB%8B%A4%EC%A4%91-JPA-mysql-postgresql-%EC%98%88%EC%A0%9C
}
