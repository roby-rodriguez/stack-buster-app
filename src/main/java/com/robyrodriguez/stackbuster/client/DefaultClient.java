package com.robyrodriguez.stackbuster.client;

import com.robyrodriguez.stackbuster.transfer.RequestAnalyzerDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

/**
 * Default JAX-RS 2.0 (ex-Jersey) HTTP client
 */
@Component
public class DefaultClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClient.class);

    @Value("${server.request-analyzer.url}")
    private String requestAnalyzerURL;

    private Client client;

    public DefaultClient() throws Exception {
        client = ClientBuilder.newClient();
    }

    public RequestAnalyzerDO ping() throws Exception {
        return client
                .target(requestAnalyzerURL)
                .request(MediaType.APPLICATION_JSON)
                .get(RequestAnalyzerDO.class);
    }
}
