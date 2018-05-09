package com.robyrodriguez.stackbuster.client;

import com.robyrodriguez.stackbuster.transfer.RequestAnalyzerDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Abstract HTTP client
 */
@Component
public abstract class AbstractHttpClient {

    static final Logger LOGGER = LoggerFactory.getLogger(StackClient.class);

    @Value("${apis.request-analyzer.url}")
    private String requestAnalyzerURL;

    String getRequestAnalyzerURL() {
        return this.requestAnalyzerURL;
    }

    public abstract RequestAnalyzerDO ping() throws Exception;

    public abstract <T> T get(String url, Class<? extends T> clazz) throws Exception;
}
