package com.robyrodriguez.stackbuster;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * App launcher
 */
public class App {
    public static void main(String args[]) throws Exception {
        // Instantiate and configure the SslContextFactory
        SslContextFactory sslContextFactory = new SslContextFactory();

        // Instantiate HttpClient
        HttpClient httpClient = new HttpClient();

        // Start HttpClient
        httpClient.start();
    }
}
