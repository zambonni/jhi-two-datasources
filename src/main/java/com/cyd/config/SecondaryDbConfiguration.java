package com.cyd.config;

import com.cyd.secondary.domain.UserFoo;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.liquibase.SpringLiquibaseUtil;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.cyd.secondary.domain")
@EnableJpaRepositories(transactionManagerRef = "secondaryTransactionManager",
    entityManagerFactoryRef = "secondaryEntityManagerFactory",
    basePackages = "com.cyd.secondary.repository")
public class SecondaryDbConfiguration {

    private final Logger log = LoggerFactory.getLogger(SecondaryDbConfiguration.class);

    private final Environment env;

    public SecondaryDbConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    @ConfigurationProperties("spring.secondary.datasource")
    public DataSourceProperties secondaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.secondary.datasource")
    public DataSource secondaryDataSource() {
        return secondaryDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
        EntityManagerFactoryBuilder builder) {

        LocalContainerEntityManagerFactoryBean emf = builder
            .dataSource(secondaryDataSource())
            .packages(UserFoo.class)
            .persistenceUnit("secondary")
            .build();
        return emf;
    }

    @Bean(name = "secondaryTransactionManager")
    public JpaTransactionManager db2TransactionManager(@Qualifier("secondaryEntityManagerFactory") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public SpringLiquibase liquibaseSecondary(
        @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
        LiquibaseProperties liquibaseProperties
    ) {
        // If you don't want Liquibase to start asynchronously, substitute by this:
        // SpringLiquibase liquibase = SpringLiquibaseUtil.createSpringLiquibase(liquibaseDataSource.getIfAvailable(), liquibaseProperties, dataSource.getIfUnique(), dataSourceProperties);
        SpringLiquibase liquibase = SpringLiquibaseUtil.createSpringLiquibase(
            liquibaseDataSource.getIfAvailable(),
            liquibaseProperties,
            secondaryDataSource(),
            secondaryDataSourceProperties()
        );
        liquibase.setChangeLog("classpath:config/liquibase/master_secondary.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setLabels(liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        if (env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE))) {
            liquibase.setShouldRun(false);
        } else {
            liquibase.setShouldRun(liquibaseProperties.isEnabled());
            log.debug("Configuring secondary Liquibase");
        }
        return liquibase;
    }
}
