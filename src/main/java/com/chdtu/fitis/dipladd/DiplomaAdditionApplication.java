package com.chdtu.fitis.dipladd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@SpringBootApplication
public class DiplomaAdditionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiplomaAdditionApplication.class, args);
    }
}
