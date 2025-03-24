package com.pudding.config.db.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.pudding.config.db.DataSourceProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidProperties {


    @Resource
    private DataSourceProperties dataSourceProperties;

    private int initialSize;
    private int minIdle;
    private int maxActive;
    private int maxWait;
    private int timeBetweenEvictionRunsMillis;
    private int minEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements;
    private int maxPoolPreparedStatementPerConnectionSize;
    private String filters;
    private String connectionProperties;


    @Bean
    @Primary
    public DataSource dataSource() {

        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dataSourceProperties.getUrl());
        datasource.setUsername(dataSourceProperties.getUsername());
        datasource.setPassword(dataSourceProperties.getPassword());
        datasource.setDriverClassName(dataSourceProperties.getDriverClassName());

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {

            datasource.setFilters(filters);
        } catch (SQLException e) {

            System.err.println("druid configuration initialization filter: " + e);
        }
        datasource.setConnectionProperties(connectionProperties);
        return datasource;
    }

}
