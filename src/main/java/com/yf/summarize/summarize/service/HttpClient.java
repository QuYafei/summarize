package com.yf.summarize.summarize.service;

import com.yf.summarize.summarize.httpclient.HttpResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.Charset;
import java.util.Map;

public class HttpClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private RequestConfig config;

    @Value("${jira.url}")
    private String jiraUrl;

    @Value("${jira.user}")
    private String user;

    @Value("${jira.password}")
    private String password;

//    public ProjectDetail getProject(String projectKey) throws Exception {
//
//        HttpResult res = doGet(jiraUrl + "/rest/api/2/project/" + projectKey);
//        if (res.getCode() == 200) {
//            ProjectDetail projectDetail = JacksonUtil.json2Bean(res.getBody(), ProjectDetail.class);
//            return projectDetail;
//        } else if (res.getCode() == 401) {
//            throw new UnauthorizedException("Authentication credentials are incorrect or missing.");
//        } else if (res.getCode() == 404) {
//            throw new NotFoundException(String.format("The Project %s is not found or the user does not have permission to view it.", projectKey));
//        } else {
//            throw new Exception("Unkown Error.");
//        }
//    }



    private HttpResult doGet(String url) throws Exception {
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);
        // 装载配置信息
        httpGet.setConfig(config);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, getAuthHeader());
        httpGet.setHeader("Content-type", "application/json");
        logger.info(httpGet.toString());

        // 发起请求
        CloseableHttpResponse response = this.httpClient.execute(httpGet);
        String responseBody = getResponse(response);
        Integer responseCode = response.getStatusLine().getStatusCode();
        logger.info(String.format("response code: %d. body: %s", responseCode, responseBody));

        return new HttpResult(responseCode, responseBody);
    }

    private HttpResult doGet(String url, Map<String, Object> params) throws Exception {
        return this.doGet(getUrlWithParam(url, params));
    }

    private HttpResult doPost(String url, String postData) throws Exception {
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        // 加入配置信息
        httpPost.setConfig(config);
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, getAuthHeader());
        httpPost.setHeader("Content-type", "application/json");

        // 判断map是否为空，不为空则进行遍历，封装from表单对象
        if (postData != null && !postData.isEmpty()) {
            // 构造from表单对象
            StringEntity stringEntity = new StringEntity(postData, "UTF-8");
            // 把表单放到post里
            httpPost.setEntity(stringEntity);
        }

        // 发起请求
        CloseableHttpResponse response = this.httpClient.execute(httpPost);
        logger.info(httpPost.toString());
        logger.info(String.format("request data is: %s", postData));

        String responseBody = getResponse(response);
        Integer responseCode = response.getStatusLine().getStatusCode();
        logger.info(String.format("response code: %d. body: %s", responseCode, responseBody));

        return new HttpResult(responseCode, responseBody);
    }

    private HttpResult doPut(String url, String putData) throws Exception {
        // 声明httpPut请求
        HttpPut httpPut = new HttpPut(url);
        // 加入配置信息
        httpPut.setConfig(config);
        httpPut.setHeader(HttpHeaders.AUTHORIZATION, getAuthHeader());
        httpPut.setHeader("Content-type", "application/json");

        // 判断map是否为空，不为空则进行遍历，封装from表单对象
        if (putData != null && !putData.isEmpty()) {
            // 构造from表单对象
            StringEntity stringEntity = new StringEntity(putData, "UTF-8");
            // 把表单放到post里
            httpPut.setEntity(stringEntity);
        }

        // 发起请求
        CloseableHttpResponse response = this.httpClient.execute(httpPut);
        logger.info(httpPut.toString());
        logger.info(String.format("request data is: %s", putData));

        String responseBody = getResponse(response);
        Integer responseCode = response.getStatusLine().getStatusCode();
        logger.info(String.format("response code: %d. body: %s", responseCode, responseBody));

        return new HttpResult(responseCode, responseBody);
    }

    private HttpResult doDelete(String url, Map<String, Object> params) throws Exception {
        return this.doDelete(getUrlWithParam(url, params));
    }

    private HttpResult doDelete(String url) throws Exception {
        // 声明httpPut请求
        HttpDelete httpDelete = new HttpDelete(url);
        // 加入配置信息
        httpDelete.setConfig(config);
        httpDelete.setHeader(HttpHeaders.AUTHORIZATION, getAuthHeader());
        httpDelete.setHeader("Content-type", "application/json");
        logger.info(httpDelete.toString());

        // 发起请求
        CloseableHttpResponse response = this.httpClient.execute(httpDelete);

        String responseBody = getResponse(response);
        Integer responseCode = response.getStatusLine().getStatusCode();
        logger.info(String.format("response code: %d. body: %s", responseCode, responseBody));

        return new HttpResult(responseCode, responseBody);
    }

    private String getResponse(CloseableHttpResponse response) throws Exception  {
        HttpEntity httpEntity = response.getEntity();
        if (httpEntity != null) {
            return EntityUtils.toString(httpEntity, "UTF-8");
        }else {
            return null;
        }
    }

    private String getUrlWithParam(String url, Map<String, Object> params) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            // 遍历map,拼接请求参数
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        return uriBuilder.build().toString();
    }

    private String getAuthHeader() {
        String auth = user + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("ISO-8859-1")));
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }
}
