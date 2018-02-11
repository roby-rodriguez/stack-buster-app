package com.robyrodriguez.stackbuster.transfer;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpProxy;
import org.eclipse.jetty.client.ProxyConfiguration;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.robyrodriguez.stackbuster.utils.StringUtil;

/**
 * Stack-specific HTTP client
 */
public class StackClient {

    private static final Logger LOG = Log.getLogger(StackClient.class);

    private HttpClient httpClient;

    public StackClient() throws Exception {
        // configure SSL
        SslContextFactory sslContextFactory = new SslContextFactory();
        // create HTTP client
        httpClient = new HttpClient(sslContextFactory);
        // start HTTP client
        httpClient.start();
    }

    public boolean incrementCounter(String url) throws Exception {
        ContentResponse response = httpClient
                .newRequest(url)
                //.agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
                //.header("upgrade-insecure-requests", "1")
                .send()
                ;

        if (isSuccess(response)) {
            String ivcPath = StringUtil.extractIVC(response.getContentAsString());

            response = httpClient
                    .newRequest("https://stackoverflow.com" + ivcPath + "?_=" + System.currentTimeMillis())
                    .agent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
                    .header(HttpHeader.REFERER, "https://stackoverflow.com/")
                    .send()
            ;
            return isSuccess(response);

        }
        return false;
    }

    private boolean isSuccess(ContentResponse response) {
        return response.getStatus() >= 200 && response.getStatus() < 300;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            httpClient.stop();
        } catch (Exception e) {
            LOG.warn("Error encountered while closing stack client: {0}", e);
        }
    }
}
