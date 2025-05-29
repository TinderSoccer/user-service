package com.tindersoccer.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // Nuevo import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // Nuevo import

@SpringBootApplication
@EntityScan("com.tindersoccer.user_service.model") // Indica dónde buscar entidades JPA
@EnableJpaRepositories("com.tindersoccer.user_service.repository") // Indica dónde buscar repositorios JPA
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}