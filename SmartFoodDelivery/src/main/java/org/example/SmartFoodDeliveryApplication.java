package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
 class SmartFoodDeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartFoodDeliveryApplication.class, args);
    }
}