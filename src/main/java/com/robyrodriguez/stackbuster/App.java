package com.robyrodriguez.stackbuster;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpProxy;
import org.eclipse.jetty.client.ProxyConfiguration;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import java.util.Iterator;

/**
 * App launcher
 */
public class App {
    public static void main(String args[]) throws Exception {
        // Instantiate and configure the SslContextFactory
        SslContextFactory sslContextFactory = new SslContextFactory();

        // Instantiate HttpClient
        HttpClient httpClient = new HttpClient(sslContextFactory);

        // Start HttpClient
        httpClient.start();

        Request request = httpClient
                .newRequest("https://stackoverflow.com/q/42920606/5173530")
                //.agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
                //.header(HttpHeader.HOST, "stackoverflow.com")
                ;
        ContentResponse response = request.send();

        System.out.println(response.getStatus());
        for (Iterator<HttpField> it = response.getHeaders().iterator(); it.hasNext();) {
            HttpField header = it.next();
            System.out.println(header.getName() + ": " + header.getValue());
        }
        System.out.println(response.getContentAsString());

        httpClient.stop();
    }
}
