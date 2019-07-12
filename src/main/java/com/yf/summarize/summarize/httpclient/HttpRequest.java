package com.yf.summarize.summarize.httpclient;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private HttpServletRequest request;

    //get request headers
    private Map<String, String> getHeadersInfo() {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    //get user agent
    private String getUserAgent() {
        return request.getHeader("user-agent");
    }


}
