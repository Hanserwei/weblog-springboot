package com.hanserwei.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.hanserwei")
@EnableJpaRepositories(basePackages = "com.hanserwei.common.domain.repository")
@EntityScan(basePackages = "com.hanserwei.common.domain.dataobject")
public class WeblogWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeblogWebApplication.class, args);
    }
}
