package com.sajoproject.sajotuna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SajoTunaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SajoTunaApplication.class, args);
    }

}
