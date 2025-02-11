package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        DatabaseLinker.connect();
        SpringApplication.run(Application.class, args);
    }
}
