package com.example.hrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class HrmApplicationTests {

    public static void main(String[] args) {
        SpringApplication.run(HrmApplication.class, args);
    }

}

