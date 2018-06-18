package com.robyrodriguez.stackbuster.service;

import com.robyrodriguez.stackbuster.client.StackClient;
import com.robyrodriguez.stackbuster.transfer.RequestAnalyzerDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Cached calls to IP checker API
 */
@Service
public class RequestAnalyzerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestAnalyzerService.class);

    @Autowired
    private StackClient stackClient;

    @Cacheable(value = "requestAnalyzer")
    public RequestAnalyzerDO update() {
        RequestAnalyzerDO requestAnalyzer = null;
        try {
            requestAnalyzer = stackClient.ping();
            RequestAnalyzerService.LOGGER.info("running stack_api scheduler with proxy address {}", requestAnalyzer.getIp());
        } catch (Exception e) {
            RequestAnalyzerService.LOGGER.error("cannot reach ping server", e);
        }
        return requestAnalyzer;
    }

    @CacheEvict(value = "requestAnalyzer", allEntries = true)
    public void reset() {
    }
}
