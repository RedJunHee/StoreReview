package com.review.storereview.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/*@EnableJpaRepositories(
        basePackages = {"com.review.storereview.repository.mng"},
        entityManagerFactoryRef = "MNGEntityManagerFactory",
        transactionManagerRef = "MNGTransactionManager"
)*/
public class MngJpaConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.mng-datasource")
    public DataSource MNGDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build(); // DataSource를 build하는데 편리한 class "DataSourceBuilder"
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean MNGEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("MNGDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.review.storereview.dao.mng")
                .persistenceUnit("MNGEntityManager")
                .build();
    }
    @Bean
    public PlatformTransactionManager MNGTransactionManager(@Qualifier("MNGEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}