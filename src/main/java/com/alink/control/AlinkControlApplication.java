package com.alink.control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AlinkControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlinkControlApplication.class, args);
    }
}
