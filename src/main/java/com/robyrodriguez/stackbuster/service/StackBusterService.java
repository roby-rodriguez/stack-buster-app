package com.robyrodriguez.stackbuster.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Main service
 */
@Component
public class StackBusterService {

    @Value("${name:World}")
    private String name;

    public String getMessage() {
        return "Hello " + this.name;
    }
}
