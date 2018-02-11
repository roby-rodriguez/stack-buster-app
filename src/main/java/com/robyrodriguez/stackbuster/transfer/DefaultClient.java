package com.robyrodriguez.stackbuster.transfer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import java.io.Serializable;

import com.robyrodriguez.stackbuster.transfer.data.RequestAnalyzerDO;

/**
 * Default JAX-RS 2.0 (ex-Jersey) HTTP client
 */
public class DefaultClient implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Client client;
    private static DefaultClient instance;

    private DefaultClient() throws Exception {
        client = ClientBuilder.newClient();
    }

    public static DefaultClient getInstance() throws Exception {
        if (instance == null) {
            instance = new DefaultClient();
        }
        return instance;
    }

    public static Client getClient() {
        return client;
    }

    public static RequestAnalyzerDO get(String url) throws Exception {
        return getInstance()
                .getClient()
                .target(url)
                .request(MediaType.APPLICATION_JSON)
                .get(RequestAnalyzerDO.class);
    }
}
