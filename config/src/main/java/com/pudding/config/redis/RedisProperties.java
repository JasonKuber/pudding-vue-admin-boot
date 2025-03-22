package com.pudding.config.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    private String host;
    private Integer port;
    private String password;
    private String username;


}
