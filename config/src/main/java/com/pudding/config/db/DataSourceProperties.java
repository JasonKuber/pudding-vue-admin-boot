package com.pudding.config.db;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;



}
