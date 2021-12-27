package com.review.StoreReview.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.review.StoreReview.repository"},
        entityManagerFactoryRef = "CMSEntityManagerFactory",
        transactionManagerRef = "CMSTransactionManager"
)
public class CMSJpaConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.cms-datasource")
    public DataSourceProperties CMSDataSourceProperties() {     // property를 이용하여 생성
        return new DataSourceProperties();
    }

    @Bean
    public DataSource CMSDataSource(@Qualifier("CMSDataSourceProperties") DataSourceProperties dsProperties) {
        return dsProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean CMSEntityManagerFactory(EntityManagerFactoryBuilder builder,  // Could not autowire. No beans of 'EntityManagerFactoryBuilder' type found.
                                                                           @Qualifier("CMSDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.review.StoreReview.domain.CMS")
                .persistenceUnit("CMSEntityManager")
                .build();
    }
    @Bean
    public PlatformTransactionManager CMSTransactionManager(@Qualifier("CMSEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
