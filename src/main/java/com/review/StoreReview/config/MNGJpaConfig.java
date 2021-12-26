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
        basePackages = {"com.review.StoreReview.repository"},
        entityManagerFactoryRef = "MNGEntityManagerFactory",
        transactionManagerRef = "MNGTransactionManager"
)
public class MNGJpaConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.mng-datasource")
    public DataSourceProperties MNGDataSourceProperties() {     // property를 이용하여 생성
        return new DataSourceProperties();
    }

    @Bean
    public DataSource MNGDataSource(@Qualifier("MNGDataSourceProperties") DataSourceProperties dsProperties) {
        return dsProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean MNGEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("MNGDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.review.StoreReview.domain.MNG")
                .persistenceUnit("MNGEntityManager")
                .build();
    }
    @Bean
    public PlatformTransactionManager MNGTransactionManager(@Qualifier("MNGEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
