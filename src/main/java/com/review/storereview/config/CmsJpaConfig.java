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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.review.StoreReview.repository"},
        entityManagerFactoryRef = "CMSEntityManagerFactory",
        transactionManagerRef = "CMSTransactionManager"
)
public class CmsJpaConfig {
    /**
     *  아래와 같이 Datasource 값 세팅을 Java Config로 수동 설정할 때는 spring.datasource.jdbc-url로 설정해야 HikariCP가 인식한다. (SpringBoot 2.0부터 HikariCP가 기본으로 변경됨)
     * DB를 2개 이상 사용해야할 경우 직접 Datasource를 만들어야 함
     * @Return DataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.cms-datasource")
    public DataSource CMSDataSource() {          //
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * - LocalContainerEntityManageFactoryBean : 컨테이너가 관리하는 EntityManagerFactory 생성하여 관리
     * - LocalEntityManagerFactoryBean : 테스트 용도나 단지 JPA를 이용해서 데이터베이스에 접근하고자 할 때 사용 (어플리케이션에서 직접 관리)
     * @Param EntityManagerFactoryBuilder
     * @Param DataSource
     * @Return LocalContainerEntityManagerFactoryBean
     */

    @Bean
    public LocalContainerEntityManagerFactoryBean CMSEntityManagerFactory(EntityManagerFactoryBuilder builder,
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
