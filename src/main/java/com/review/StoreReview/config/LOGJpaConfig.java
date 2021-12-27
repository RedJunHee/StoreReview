package com.review.StoreReview.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@EnableJpaRepositories(
        basePackages = {"com.review.StoreReview.repository"},       // 해당 DataSource를 적용할 패키지 경로
        entityManagerFactoryRef = "LOGEntityManagerFactory",
        transactionManagerRef = "LOGTransactionManager"
)
public class LOGJpaConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.log-datasource")
    public DataSourceProperties LOGDataSourceProperties() {     // property를 이용하여 생성
        return new DataSourceProperties();
    }

    @Bean
    public DataSource LOGDataSource(@Qualifier("LOGDataSourceProperties") DataSourceProperties dsProperties) {
        return dsProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean LOGEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("LOGDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.review.StoreReview.domain.LOG")
                .persistenceUnit("LOGEntityManager")
                .build();
    }
    @Bean
    public PlatformTransactionManager LOGTransactionManager(@Qualifier("LOGEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
