package com.robyrodriguez.stackbuster.client;

import com.robyrodriguez.stackbuster.transfer.RequestAnalyzerDO;
import org.glassfish.jersey.client.filter.EncodingFilter;
import org.glassfish.jersey.message.GZipEncoder;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

/**
 * Default JAX-RS 2.0 (ex-Jersey, general purpose) HTTP client
 */
@Component
public class DefaultClient extends AbstractHttpClient {

    private Client client;

    public DefaultClient() throws Exception {
        client = ClientBuilder.newClient();
        client.register(GZipEncoder.class);
        client.register(EncodingFilter.class);
    }

    public <T> T get(String url, Class<? extends T> clazz) throws Exception {
        LOGGER.info("default client lookup: {}", url);
        return client
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .get(clazz);
    }

    public RequestAnalyzerDO ping() throws Exception {
        LOGGER.info("default client ping: {}", getRequestAnalyzerURL());
        return client
                .target(getRequestAnalyzerURL())
                .request(MediaType.APPLICATION_JSON)
                .get(RequestAnalyzerDO.class);
    }
}
