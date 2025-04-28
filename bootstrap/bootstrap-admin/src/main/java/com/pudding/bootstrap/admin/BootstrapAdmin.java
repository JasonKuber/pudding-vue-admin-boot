package com.pudding.bootstrap.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.pudding"})
@MapperScan("com.pudding.repository.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BootstrapAdmin {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapAdmin.class,args);
        log.info("Pudding BootstrapAdmin启动成功~!");
    }

}
