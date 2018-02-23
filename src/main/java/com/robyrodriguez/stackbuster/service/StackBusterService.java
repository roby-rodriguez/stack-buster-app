package com.robyrodriguez.stackbuster.service;

import com.robyrodriguez.stackbuster.client.DefaultClient;
import com.robyrodriguez.stackbuster.transfer.data.RequestAnalyzerDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Main service
 */
@Component
public class StackBusterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackBusterService.class);

    @Value("${name:World}")
    private String name;

    @Autowired
    private DefaultClient client;

    public String getMessage() {
        try {
            RequestAnalyzerDO request = client.ping();
            LOGGER.info(request.toString());
        } catch (Exception e) {
            LOGGER.warn("Encountered error: {}", e);
        }
        return "Hello " + this.name;
    }
}
