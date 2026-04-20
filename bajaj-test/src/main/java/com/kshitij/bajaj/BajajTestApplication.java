package com.kshitij.bajaj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.kshitij.bajaj.service.ApiService;

@SpringBootApplication
public class BajajTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BajajTestApplication.class, args);
    }

    @Bean
    CommandLineRunner run(ApiService apiService) {
        return args -> {
            apiService.startProcess();
        };
    }
}