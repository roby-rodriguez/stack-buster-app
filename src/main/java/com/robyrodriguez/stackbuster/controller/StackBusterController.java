package com.robyrodriguez.stackbuster.controller;

import com.robyrodriguez.stackbuster.service.StackBusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Main controller
 */
@Controller
public class StackBusterController {

    @Autowired
    private StackBusterService stackBusterService;

    @GetMapping
    @ResponseBody
    public String get() {
        return this.stackBusterService.getMessage();
    }
}
