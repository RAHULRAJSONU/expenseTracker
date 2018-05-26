package com.silvertech.expenseTracker.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.beans.PropertyVetoException;

@Configuration
@ConfigurationProperties(prefix = "expense-tracker.mariadb")
@Setter
@Profile("!stub")
public class DatasourceConfiguration {
    private int maxPoolSize;
    private int minPoolSize;
    private int acquireIncrement;
    private int idleTestPeriod;
    private int maxStatements;
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;

    @Bean
    @Profile("!stub")
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setMaxPoolSize(this.maxPoolSize);
        dataSource.setMinPoolSize(this.minPoolSize);
        dataSource.setAcquireIncrement(this.acquireIncrement);
        dataSource.setIdleConnectionTestPeriod(this.idleTestPeriod);
        dataSource.setMaxStatements(this.maxStatements);
        dataSource.setJdbcUrl(this.jdbcUrl);
        dataSource.setPassword(this.password);
        dataSource.setUser(this.username);
        dataSource.setDriverClass(this.driverClassName);
        return dataSource;
    }
}
