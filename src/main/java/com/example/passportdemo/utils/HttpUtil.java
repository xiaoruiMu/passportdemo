package com.example.passportdemo.utils;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.Map;

/**
 * HttpUtil
 *
 * @author muxiaorui
 * @create 2018-06-29 15:29
 **/
public class HttpUtil {
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "Content-Type";
    public static final String HTTP_ERROR_PREFIX = "passport";
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int SO_TIMEOUT = 180000;
    /**
     * HttpResponse包装类
     */
    public static class HttpResponseWrapper {
        private HttpResponse httpResponse;
        private final HttpClient httpClient;

        public HttpResponseWrapper(HttpClient httpClient, HttpResponse httpResponse) {
            this.httpClient = httpClient;
            this.httpResponse = httpResponse;
        }

        public HttpResponseWrapper(HttpClient httpClient) {
            this.httpClient = httpClient;
        }
        public HttpResponse getHttpResponse() {
            return httpResponse;
        }
        public void setHttpResponse(HttpResponse httpResponse) {
            this.httpResponse = httpResponse;
        }

        /**
         * 获得流类型的响应
         */
        public InputStream getResponseStream() throws IllegalStateException, IOException {
            return httpResponse.getEntity().getContent();
        }

        /**
         * 获得字符串类型的响应
         */
        public String getResponseString(String responseCharacter) throws ParseException, IOException {
            HttpEntity entity = getEntity();
            String responseStr = EntityUtils.toString(entity, responseCharacter);
            if (entity.getContentType() == null) {
                responseStr = new String(responseStr.getBytes("iso-8859-1"), responseCharacter);
            }
            EntityUtils.consume(entity);
            return responseStr;
        }

        public String getResponseString() throws ParseException, IOException {
            return getResponseString(CHARACTER_ENCODING);
        }

        /**
         * 获得响应状态码
         */
        public int getStatusCode() {
            return httpResponse.getStatusLine().getStatusCode();
        }

        /**
         * 获得响应状态码并释放资源
         */
        public int getStatusCodeAndClose() {
            close();
            return getStatusCode();
        }

        public HttpEntity getEntity() {
            return httpResponse.getEntity();
        }

        /**
         * 释放资源
         */
        public void close() {
            httpClient.getConnectionManager().shutdown();
        }
    }
    /**
     * PUT方式提交Json数据
     */
    public static String requestPutJson(String url, String json, String requestCharacter, String responseCharacter,int connectionTimeout,int soTimeout) {
        HttpResponseWrapper httpResponseWrapper = null;
        String responseStr = "";
        try {
            httpResponseWrapper = requestPutJsonResponse(url, json, requestCharacter,connectionTimeout,soTimeout);
            responseStr = httpResponseWrapper.getResponseString(responseCharacter);
        } catch (Exception e) {
            responseStr = HTTP_ERROR_PREFIX + e.toString();
            System.out.println("requestPostForm request Exception:" +e);
        } finally {
            if(httpResponseWrapper != null){
                httpResponseWrapper.close();
            }
        }
        return responseStr;
    }

    /**
     * POST方式提交Json数据
     */
    public static String requestPostJson(String url, String json, String requestCharacter, String responseCharacter,int connectionTimeout,int soTimeout) {
        HttpResponseWrapper httpResponseWrapper = null;
        String responseStr = "";
        try {
            httpResponseWrapper = requestPostJsonResponse(url, json, requestCharacter,connectionTimeout,soTimeout);
            responseStr = httpResponseWrapper.getResponseString(responseCharacter);
        } catch (Exception e) {
            responseStr = HTTP_ERROR_PREFIX + e.toString();
            System.out.println("requestPostForm request Exception:" +e);
        } finally {
            if(httpResponseWrapper != null){
                httpResponseWrapper.close();
            }
        }
        return responseStr;
    }

    /**
     * POST方式提交JSON数据
     */
    public static String requestPostJson(String url, String json) {
        return requestPostJson(url, json, CHARACTER_ENCODING, CHARACTER_ENCODING,CONNECTION_TIMEOUT,SO_TIMEOUT);
    }

    /**
     * PUT方式提交Json数据，返回响应对象
     * @throws Exception
     */
    private static HttpResponseWrapper requestPutJsonResponse(String url, String json, String requestCharacter,int connectionTimeout,int soTimeout) throws Exception {
        HttpClient client = null;
        if (url.startsWith("https")) {
            client =createHttpsClient(connectionTimeout,soTimeout);
        }else{
            client =createHttpClient(connectionTimeout,soTimeout);
        }

        String contentType = "application/json; charset=" + requestCharacter;
        HttpPut httpPost = new HttpPut(url);
        httpPost.setHeader(CONTENT_TYPE, contentType);
        StringEntity se =  new StringEntity(json,requestCharacter);
        httpPost.setEntity(se);
        int retryTimes = 3;
        HttpResponse httpResponse = null; //执行POST请求

        while(retryTimes > 0) {
            try {
                httpResponse = client.execute(httpPost);
                break;
            } catch (IOException e) {
                if(retryTimes <= 1) {
                    throw e;
                }
            } //执行POST请求
            retryTimes --;
        }
        return new HttpResponseWrapper(client, httpResponse);
    }

    /**
     * POST方式提交Json数据，返回响应对象
     * @throws Exception
     */
    private static HttpResponseWrapper requestPostJsonResponse(String url, String json, String requestCharacter,int connectionTimeout,int soTimeout) throws Exception {
        HttpClient client = null;
        if (url.startsWith("https")) {
            client =createHttpsClient(connectionTimeout,soTimeout);
        }else{
            client =createHttpClient(connectionTimeout,soTimeout);
        }

        String contentType = "application/json; charset=" + requestCharacter;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(CONTENT_TYPE, contentType);
        StringEntity se =  new StringEntity(json,requestCharacter);
        httpPost.setEntity(se);
        int retryTimes = 3;
        HttpResponse httpResponse = null; //执行POST请求

        while(retryTimes > 0) {
            try {
                httpResponse = client.execute(httpPost);
                break;
            } catch (IOException e) {
                if(retryTimes <= 1) {
                    throw e;
                }
            } //执行POST请求
            retryTimes --;
        }
        return new HttpResponseWrapper(client, httpResponse);
    }


    private static HttpClient createHttpsClient(int connectionTimeout,int soTimeout) throws Exception  {
        HttpClient httpClient = new DefaultHttpClient(); //创建默认的httpClient实例
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, soTimeout);
        //TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
        SSLContext ctx = SSLContext.getInstance("TLS");
        //使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
        ctx.init(null, new TrustManager[]{new TrustAnyTrustManager()}, null);
        //创建SSLSocketFactory
        SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
        //通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
        httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
        return httpClient;
    }

    private static HttpClient createHttpClient(int connectionTimeout,int soTimeout) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, soTimeout);
        return httpClient;
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }
    /**
     * GET方式提交URL请求，会自动重定向 返回responseBody信息
     */
    public static String requestGetNew(String url, String responseCharacter ,int connectionTimeout,int soTimeout,Map<String,Object> map) {
        HttpResponseWrapper httpResponseWrapper = null;
        String responseStr = "";
        try {
            HttpClient client = null;
            if (url.startsWith("https")) {
                client =createHttpsClient(connectionTimeout,soTimeout);
            }else{
                client =createHttpClient(connectionTimeout,soTimeout);
            }
            HttpGet httpGet = new HttpGet(url);
            if(map !=null && map.size()>0){
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    httpGet.setHeader( entry.getKey(),entry.getValue().toString());
                }
            }
            HttpResponse httpResponse = client.execute(httpGet);
            httpResponseWrapper=new HttpResponseWrapper(client, httpResponse);
            responseStr = httpResponseWrapper.getResponseString(responseCharacter);
        } catch (Exception e) {
            responseStr  = HTTP_ERROR_PREFIX + e.toString();
            System.out.println("requstGet request Exception:"+e);
        } finally {
            if(httpResponseWrapper != null){
                httpResponseWrapper.close();
            }
        }
        return responseStr;
    }
    /**
     * GET方式提交URL请求，会自动重定向 返回responseBody信息
     */
    public static String requestGetNew(String url,Map<String,Object> map) {
        return requestGetNew(url, CHARACTER_ENCODING ,CONNECTION_TIMEOUT,SO_TIMEOUT,map);
    }


    public static String requestPostJsonWithHeader(String url, String json,Map<String,Object> map) {
        return requestPostJsonWithHeader(url, json, CHARACTER_ENCODING, CHARACTER_ENCODING,CONNECTION_TIMEOUT,SO_TIMEOUT,map);
    }
    /**
     * psot json方式提交URL请求，会自动重定向 包涵头信息
     */

    public static String requestPostJsonWithHeader(String url, String json, String requestCharacter, String responseCharacter,int connectionTimeout,int soTimeout,Map<String,Object> map) {
        HttpResponseWrapper httpResponseWrapper = null;
        String responseStr = "";
        try {
            HttpClient client = null;
            if (url.startsWith("https")) {
                client =createHttpsClient(connectionTimeout,soTimeout);
            }else{
                client =createHttpClient(connectionTimeout,soTimeout);
            }

            String contentType = "application/json; charset=" + requestCharacter;
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(CONTENT_TYPE, contentType);
            if(map !=null && map.size()>0){
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    httpPost.setHeader( entry.getKey(),entry.getValue().toString());
                }
            }
            StringEntity se =  new StringEntity(json,requestCharacter);
            httpPost.setEntity(se);
            HttpResponse httpResponse = client.execute(httpPost); //执行POST请求
            httpResponseWrapper= new HttpResponseWrapper(client, httpResponse);

            responseStr = httpResponseWrapper.getResponseString(responseCharacter);
        } catch (Exception e) {
            responseStr = HTTP_ERROR_PREFIX + e.toString();
            System.out.println("requestPostForm request Exception:" +e);
        } finally {
            if(httpResponseWrapper != null){
                httpResponseWrapper.close();
            }
        }
        return responseStr;
    }


}
