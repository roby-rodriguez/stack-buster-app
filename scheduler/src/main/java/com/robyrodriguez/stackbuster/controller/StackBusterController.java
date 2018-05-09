package com.robyrodriguez.stackbuster.controller;

import com.robyrodriguez.stackbuster.service.StackBusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main controller
 */
@RestController
public class StackBusterController {

    @Autowired
    private StackBusterService stackBusterService;

    @GetMapping
    public String get() {
        return this.stackBusterService.getMessage();
    }
}
