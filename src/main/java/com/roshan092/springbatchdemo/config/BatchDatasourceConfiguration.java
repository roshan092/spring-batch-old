package com.roshan092.springbatchdemo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hikari.datasource.spring_batch")
public class BatchDatasourceConfiguration extends HikariConfig {

    @Bean(name = "batchDataSource", destroyMethod = "close")
    public HikariDataSource batchDataSource() {
        return new HikariDataSource(this);
    }

}
