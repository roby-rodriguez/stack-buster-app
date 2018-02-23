package com.robyrodriguez.stackbuster.service;

import com.robyrodriguez.stackbuster.client.DefaultClient;
import com.robyrodriguez.stackbuster.client.StackClient;
import com.robyrodriguez.stackbuster.transfer.RequestAnalyzerDO;
import com.robyrodriguez.stackbuster.types.BadgeType;
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
    private DefaultClient defaultClient;

    @Autowired
    private StackClient stackClient;

    public String getMessage() {
        try {
            RequestAnalyzerDO request = defaultClient.ping();
            LOGGER.info(request.toString());
        } catch (Exception e) {
            LOGGER.warn("Encountered error: {}", e);
        }
        return "Hello " + this.name;
    }

    /**
     * Adds a new question for the given user for batch processing
     *
     * @param question the question
     * @param userId user id
     * @param badgeType badge type
     */
    public void enqueue(String question, String userId, BadgeType badgeType) {

    }

    /**
     * Starts batch processing of questions
     *
     * @throws Exception
     */
    public void start() throws Exception {
        //TODO see TaskExecutor
    }
}
