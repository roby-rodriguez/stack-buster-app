package com.robyrodriguez.stackbuster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO add SSL support (see spring-boot-sample-tomcat-ssl) + implement API key with file
 */
@SpringBootApplication
public class StackBusterApp {

    public static void main(String[] args) {
        SpringApplication.run(StackBusterApp.class, args);
    }
}
