package com.cyd.config;

import com.cyd.secondary.domain.UserFoo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.cyd.secondary.domain")
@EnableJpaRepositories(transactionManagerRef = "partnerTransactionManager",
    entityManagerFactoryRef = "partnerEntityManagerFactory",
    basePackages = "com.cyd.secondary.repository")
public class SecondaryDbConfiguration {

    @Bean
    @ConfigurationProperties("spring.secondary.datasource")
    public DataSourceProperties partnerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.secondary.datasource")
    public DataSource partnerDataSource() {
        return partnerDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "partnerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(
        EntityManagerFactoryBuilder builder) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        LocalContainerEntityManagerFactoryBean emf = builder
            .dataSource(partnerDataSource())
            .packages(UserFoo.class)
            .persistenceUnit("secondary")
            .build();
//        emf.setJpaProperties(properties);
        return emf;
    }

    @Bean(name = "partnerTransactionManager")
    public JpaTransactionManager db2TransactionManager(@Qualifier("partnerEntityManagerFactory") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}
