package com.robyrodriguez.stackbuster.client;

import com.robyrodriguez.stackbuster.constants.Agent;
import com.robyrodriguez.stackbuster.utils.StringUtil;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.ProxyConfiguration;
import org.eclipse.jetty.client.Socks4Proxy;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Stack-specific HTTP client
 */
@Component
public class StackClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackClient.class);

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

    @PostConstruct
    public void init() throws Exception {
        LOGGER.info("Starting stack HTTP client");
        httpClient.start();
    }

    @PreDestroy
    public void destroy() throws Exception {
        LOGGER.info("Stopping stack HTTP client");
        httpClient.stop();
    }

    /**
     * Increments question page views counter
     *
     * @param url question link
     * @return true if success
     * @throws Exception
     */
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
                    .header(HttpHeader.REFERER, "https://stackoverflow.com/")
                    .send()
            ;
            return isSuccess(response);
        }
        return false;
    }

    /**
     * Checks response state
     *
     * @param response the response
     * @return true if success
     */
    private boolean isSuccess(ContentResponse response) {
        return response.getStatus() >= 200 && response.getStatus() < 300;
    }
}
