package com.review.StoreReview.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
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

    /**
     * LocalContainerEntityManagerFactoryBean : 스프링이 제공하는 컨테이너 관리 EntityManager를 위한 팩토리 빈 객체
     *
     * EntityManagerFactoryBean객체로는 아래와 같이 두가지 존재..
     * 1. LocalContainerEntityManageFactoryBean : 애플리케이션 내에서 유연한 로컬 구성을 허용하는 가장 강력한 JPA 설정 옵션.
     * 기존 JDBC DataSource에 대항 링크를 지원하고 로컬 및 글로벌 트랙잭션을 모두 지원.
     * 2. LocalEntityManagerFactoryBean : 가장 단순하고 가장 제한적. 기존 JDBC DataSource Bean 정의를 참조할 수 없다.
     * 전역 트랜잭션에 대한 지원도 없다.
     *
     * 따라서 JPA EntityManagerFactoryBean 생성시에  LocalContainerEntityManagerFactoryBean 사용하는 것이 좋음.
     * @param builder
     * @param dataSource
     * @return
     */
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean CUSTEntityManagerFactory(EntityManagerFactoryBuilder builder, // Could not autowire. No beans of 'EntityManagerFactoryBuilder' type found.
                                                                           @Qualifier("CUSTDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource) // JDBC DataSource
                .packages("com.review.StoreReview.domain.CUST") // 관리할 Entity packages
                .persistenceUnit("CUSTEntityManager") // 종속유닛의 이름을 정의.
                .build();  // DataSource + Entity Package + 유닛 이름을 가지고 EntityManageFactory 만들어냄.
    }

    /**
     * PlatformTransactionManager : 데이터 액세스 기술에 따라 사용할 수 있는 트랜잭션 추상화 클래스의 한 종류
     * 아래와 같은 총 3가지의 서비스가 있다.
     * PlatformTransactionManager.getTransaction(...)
     * PlatformTransactionManager.commit(...)
     * PlatformTransactionManager.rollback(...)
     * @param entityManagerFactory
     * @return Jpa트랜잭션 기술을 사용할 수 있는 매니저 리턴.
     * DataSourceTransactionManager, JmsTransactionManager 존재.
     */
    @Primary
    @Bean
    public PlatformTransactionManager CUSTTransactionManager(@Qualifier("CUSTEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    // 참고 : https://lemontia.tistory.com/967
    // 참고2 : https://suho413.tistory.com/entry/Spring-Boot-%EB%8B%A4%EC%A4%91-JPA-mysql-postgresql-%EC%98%88%EC%A0%9C
}
