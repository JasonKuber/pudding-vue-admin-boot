package com.pudding.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BootstrapAdmin {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapAdmin.class,args);
        log.info("Pudding BootstrapAdmin启动成功~!");
    }

}
