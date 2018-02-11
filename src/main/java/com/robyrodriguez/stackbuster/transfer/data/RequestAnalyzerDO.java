package com.robyrodriguez.stackbuster.transfer.data;

import java.util.Map;
import java.io.Serializable;

public class RequestAnalyzerDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, String> headers;
    private String ip;

    public RequestAnalyzerDO() {
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        this.headers.forEach((k, v) -> sb.append(k).append(": ").append(v).append(", "));
        return "RequestAnalyzerDO{" + "headers: {" + sb.toString() + "}, ip: '" + ip + '\'' + '}';
    }

}
