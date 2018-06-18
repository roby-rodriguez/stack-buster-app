package com.robyrodriguez.stackbuster.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robyrodriguez.stackbuster.api.StackApi;
import com.robyrodriguez.stackbuster.constants.Agent;
import com.robyrodriguez.stackbuster.exception.StackResourceNotFoundException;
import com.robyrodriguez.stackbuster.transfer.RequestAnalyzerDO;
import com.robyrodriguez.stackbuster.utils.StringUtil;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.ProxyConfiguration;
import org.eclipse.jetty.client.Socks4Proxy;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Stack-specific HTTP client
 */
@Component
public class StackClient extends AbstractHttpClient {

    private HttpClient httpClient;

    public StackClient() throws Exception {
        // configure SSL
        SslContextFactory sslContextFactory = new SslContextFactory(true);
        // create HTTP client
        httpClient = new HttpClient(sslContextFactory);
        // configure SOCKS proxy
        ProxyConfiguration proxyConfig = httpClient.getProxyConfiguration();
        // Socks4Proxy proxy = new Socks4Proxy("localhost", 9050);
        // add proxy to configuration
        // proxyConfig.getProxies().add(proxy);
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
     *
     * @param qid question id
     * @throws Exception otherwise
     */
    public void incrementCounter(String qid) throws Exception {
        incrementCounter(qid, null);
    }

    /**
     * Increments question page views counter
     *
     * @param qid question id
     * @param uid stack user id
     * @throws Exception otherwise
     */
    public void incrementCounter(String qid, String uid) throws Exception {
        String agent = Agent.randomAgent(),
               questionUrl = StackApi.QUESTION_LINK(qid, uid);
        LOGGER.info("stack client increment counter for {}", questionUrl);
        ContentResponse response = httpClient
                .newRequest(questionUrl)
                .agent(agent)
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "en-US,en;q=0.5")
                .header("connection", "keep-alive")
                .header("host", "stackoverflow.com")
                .header("upgrade-insecure-requests", "1")
                .send()
                ;

        if (uid != null && isSuccess(response)) {
            String ivcPath = StringUtil.extractIVC(response.getContentAsString()),
                   ivcUrl = StackApi.IVC_LINK(ivcPath);
            LOGGER.info("stack client increment counter ivc: {}", ivcUrl);
            response = httpClient
                    .newRequest(ivcUrl)
                    .agent(agent)
                    .header("accept", "*/*")
                    .header("accept-encoding", "gzip, deflate, br")
                    .header("accept-language", "en-US,en;q=0.5")
                    .header("connection", "keep-alive")
                    .header("host", "stackoverflow.com")
                    .header(HttpHeader.REFERER, "https://stackoverflow.com/")
                    .send()
            ;
        }

        resolution(response);
    }

    public RequestAnalyzerDO ping() throws Exception {
        LOGGER.info("stack client ping: {}", getRequestAnalyzerURL());
        ContentResponse response = httpClient
                .newRequest(getRequestAnalyzerURL())
                .send();
        if (isSuccess(response)) {
            ObjectMapper objectMapper = new ObjectMapper();
            RequestAnalyzerDO requestAnalyzer = objectMapper.readValue(response.getContent(), RequestAnalyzerDO.class);
            return  requestAnalyzer;
        }
        return null;
    }

    //TODO mix this wth resolution()
    public <T> T get(String url, Class<? extends T> clazz) throws Exception {
        LOGGER.info("stack client lookup: {}", url);
        ContentResponse response = httpClient
                .newRequest(url)
                .send();
        if (isSuccess(response)) {
            ObjectMapper objectMapper = new ObjectMapper();
            T t = objectMapper.readValue(response.getContent(), clazz);
            return t;
        } else if (response.getStatus() == 404)
            throw new StackResourceNotFoundException();
        throw new Exception(String.format("Stack API/client problem. Reason '%s' status '%d'",
            response.getReason(), response.getStatus()));
    }

    /**
     * Checks if response is success, throws exception otherwise
     *
     * @param response the response
     * @throws Exception if response not success
     */
    private void resolution(ContentResponse response) throws Exception {
        if (isSuccess(response))
            return;
        if (response.getStatus() == 404)
            throw new StackResourceNotFoundException();
        throw new Exception(String.format("Stack API/client exception. Reason '%s' status '%d'",
                response.getReason(), response.getStatus()));
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
