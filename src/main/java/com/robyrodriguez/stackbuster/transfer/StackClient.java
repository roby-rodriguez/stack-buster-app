package com.robyrodriguez.stackbuster.transfer;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.Socks4Proxy;
import org.eclipse.jetty.client.ProxyConfiguration;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.robyrodriguez.stackbuster.utils.StringUtil;
import com.robyrodriguez.stackbuster.constants.Agent;

/**
 * Stack-specific HTTP client
 */
public class StackClient {

    private static final Logger LOG = Log.getLogger(StackClient.class);

    private HttpClient httpClient;

    public StackClient() throws Exception {
        // configure SSL
        SslContextFactory sslContextFactory = new SslContextFactory(true);
        // create HTTP client
        httpClient = new HttpClient(sslContextFactory);
        // configure SOCKS proxy
        ProxyConfiguration proxyConfig = httpClient.getProxyConfiguration();
        Socks4Proxy proxy = new Socks4Proxy("localhost", 9050);
        // add proxy to configuration
        proxyConfig.getProxies().add(proxy);
        // start HTTP client
        httpClient.start();
    }

    public boolean incrementCounter(String url) throws Exception {
        String agent = Agent.randomAgent();
        ContentResponse response = httpClient
                .newRequest(url)
                .agent(agent)
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "en-US,en;q=0.5")
                .header("connection", "keep-alive")
                .header("host", "stackoverflow.com")
                .header("upgrade-insecure-requests", "1")
                .send()
                ;

        if (isSuccess(response)) {
            String ivcPath = StringUtil.extractIVC(response.getContentAsString());

            response = httpClient
                    .newRequest("https://stackoverflow.com" + ivcPath + "?_=" + System.currentTimeMillis())
                    .agent(agent)
                    .header("accept", "*/*")
                    .header("accept-encoding", "gzip, deflate, br")
                    .header("accept-language", "en-US,en;q=0.5")
                    .header("connection", "keep-alive")
                    .header("host", "stackoverflow.com")
                    .header(HttpHeader.REFERER, "https://stackoverflow.com/questions/42920606/spring-value-empty-list-as-default")
                    .send()
            ;
            return isSuccess(response);
        }
        return false;
    }

    public void makeSharedLink(String questionId, String userId) throws Exception {
        ContentResponse response = httpClient
                .newRequest("https://stackoverflow.com/gps/event")
                .send()
                ;
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
