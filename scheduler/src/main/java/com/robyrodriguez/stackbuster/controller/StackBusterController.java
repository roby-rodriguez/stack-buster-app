package com.robyrodriguez.stackbuster.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main controller
 */
@RestController
public class StackBusterController {

    @GetMapping
    public String get() {
        return "ok";
    }
}
