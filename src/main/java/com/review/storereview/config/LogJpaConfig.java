package com.review.storereview.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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


/*@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.review.storereview.repository.log"},       // 해당 DataSource를 적용할 패키지 경로
        entityManagerFactoryRef = "LOGEntityManagerFactory",
        transactionManagerRef = "LOGTransactionManager"
)*/
public class LogJpaConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.log-datasource")
    public DataSource LOGDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean LOGEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("LOGDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.review.storereview.dao.log")
                .persistenceUnit("LOGEntityManager")
                .build();
    }
    @Bean
    public PlatformTransactionManager LOGTransactionManager(@Qualifier("LOGEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
