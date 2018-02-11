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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.robyrodriguez.stackbuster.transfer.DefaultClient;

/**
 * App launcher
 */
public class App {

    /**
     * Extracts ivc URL path from html source
     *
     * @param html source to scrap
     * @return ivc URL path
     */
    public static String extractIVC(String html) {
        String result = null;
        Pattern pattern = Pattern.compile("StackExchange\\.ready\\(function\\(\\)\\{\\$\\.get\\('(.+?)'\\);}\\);");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    public static void main(String args[]) throws Exception {
        // Instantiate and configure the SslContextFactory
        SslContextFactory sslContextFactory = new SslContextFactory();

        // Instantiate HttpClient
        HttpClient httpClient = new HttpClient(sslContextFactory);

        // Start HttpClient
        httpClient.start();

        Request request = httpClient
                .newRequest("https://stackoverflow.com/q/25649725/5173530")
                .agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
                .header("upgrade-insecure-requests", "1")
                ;

        ContentResponse response = request.send();

        System.out.println(response.getStatus());
        for (Iterator<HttpField> it = response.getHeaders().iterator(); it.hasNext();) {
            HttpField header = it.next();
            System.out.println(header.getName() + ": " + header.getValue());
        }
        System.out.println("-------------------");
        System.out.println(response.getContentAsString());
        System.out.println("-------------------");

        System.out.println(DefaultClient.get("https://stack-buster-*******.codeanyapp.com/"));
/*
        // if (response.getReason() == ) {
        if (true) {
            String ivcPath = App.extractIVC(response.getContentAsString());

            response = httpClient
                .newRequest("https://stackoverflow.com" + ivcPath + "?_=" + System.currentTimeMillis())
                .agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0".replace("\n","").replace("\r",""))
                .header(HttpHeader.REFERER, "https://stackoverflow.com/")
                .send()
            ;

            System.out.println(response.getStatus());
            for (Iterator<HttpField> it = response.getHeaders().iterator(); it.hasNext();) {
                HttpField header = it.next();
                System.out.println(header.getName() + ": " + header.getValue());
            }

        }
*/
        httpClient.stop();
    }
}
