package com.demo.commons.tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http工具
 */
public class HttpClientUtil {

    /**
     * HttpClient
     */
    private static CloseableHttpClient httpClient;

    /**
     * 连接超时时间(ms)
     */
    private static int connectTimeout = 2000;
    /**
     * socket响应超时时间(ms)
     */
    private static int socketTimeout = 2000;
    /**
     * 每个host最多可以有多少连接
     */
    private static int maxConnectionsPerHost = 50;
    /**
     * 所有Host总共连接数
     */
    private static int maxTotalConnections = 1000;

    static {
        init();
    }

    private static void init() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(maxConnectionsPerHost);
        connManager.setMaxTotal(maxTotalConnections);

        RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();

        httpClient = HttpClientBuilder.create().setConnectionManager(connManager).setDefaultRequestConfig(config).build();
    }

    /**
     * 使用GET方式请求指定URI并以字符串形式获取响应内容，响应的文本内容根据Content-Type头来解码，如果没有此Header则默认使用ISO-8859-1来解码
     *
     * @param url
     * @param params
     * @return 响应内容
     */
    public static String getUriContentUsingGet(String url, Map<String, String> params) {
        return getUriRequestContent(url, true, params, null, null);
    }

    /**
     * 使用POST方式请求指定URI并返回相应内容
     *
     * @param url
     * @param params
     * @param charset
     * @return
     */
    public static String getUriContentUsingPost(String url, Map<String, String> params, String charset) {
        return getUriRequestContent(url, false, params, charset, null);
    }

    /**
     * 以GET或POST方法请求指定URI并以字符串形式获取响应内容，响应的文本内容根据Content-Type头来解码，如果没有此Header则默认使用ISO-8859-1来解码
     *
     * @param url     请求地址
     * @param useGet  是否使用GET方式
     * @param params  请求参数
     * @param charset POST时指定的参数编码，如果为空则默认使用ISO-8859-1编码参数
     * @return 响应内容，字符串的解码字符集首先从响应头Content-Type头获取，如果没有此Header则默认使用ISO-8859-1来解码
     */
    public static String getUriRequestContent(String url, boolean useGet, Map<String, String> params, String charset) {
        return getUriRequestContent(url, useGet, params, charset, null);
    }

    public static String getUriRequestContent(String url, boolean useGet, Map<String, String> params, String charset,
                                              Map<String, String> headersMap) {
        HttpUriRequest request = null;
        try {
            // GET request
            if (useGet) {
                if (params != null && params.size() > 0) {
                    URIBuilder builder = new URIBuilder(url);
                    for (Map.Entry<String, String> param : params.entrySet()) {
                        builder.addParameter(param.getKey(), param.getValue());
                    }
                    request = new HttpGet(builder.build());
                } else {
                    request = new HttpGet(url);
                }
            }
            // POST request
            else {
                request = new HttpPost(url);
                HttpPost post = (HttpPost) request;
                if (params != null && params.size() > 0) {
                    List<NameValuePair> nvList = new ArrayList<NameValuePair>();
                    for (Map.Entry<String, String> param : params.entrySet()) {
                        nvList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                    }
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvList, charset);
                    post.setEntity(formEntity);
                }
            }
            // add header if exist
            if (headersMap != null && !headersMap.isEmpty()) {
                for (Map.Entry<String, String> header : headersMap.entrySet()) {
                    request.addHeader(header.getKey(), header.getValue());
                }
            }
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            } else {
                return EntityUtils.toString(entity);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ((HttpRequestBase) request).releaseConnection();
        }
    }

    public static <T> T requestURI(String url, boolean useGet, Map<String, String> params, String charset,
                                   Map<String, String> headersMap, ResponseHandler<? extends T> responseHandler) {
        HttpUriRequest request = null;
        try {
            // GET request
            if (useGet) {
                if (params != null && params.size() > 0) {
                    URIBuilder builder = new URIBuilder(url);
                    for (Map.Entry<String, String> param : params.entrySet()) {
                        builder.addParameter(param.getKey(), param.getValue());
                    }
                    request = new HttpGet(builder.build());
                } else {
                    request = new HttpGet(url);
                }
            }
            // POST request
            else {
                request = new HttpPost(url);
                HttpPost post = (HttpPost) request;
                if (params != null && params.size() > 0) {
                    List<NameValuePair> nvList = new ArrayList<NameValuePair>();
                    for (Map.Entry<String, String> param : params.entrySet()) {
                        nvList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                    }
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvList, charset);
                    post.setEntity(formEntity);
                }
            }
            // add header if exist
            if (headersMap != null && !headersMap.isEmpty()) {
                for (Map.Entry<String, String> header : headersMap.entrySet()) {
                    request.addHeader(header.getKey(), header.getValue());
                }
            }
            T result = httpClient.execute(request, responseHandler);
            return result;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ((HttpRequestBase) request).releaseConnection();
        }
    }

    public static <T> T requestUriUsingGet(String uri, Map<String, String> params, ResponseHandler<? extends T> responseHandler) {
        return requestURI(uri, true, params, null, null, responseHandler);
    }

    public static void main(String[] args) throws URISyntaxException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("app", "2");
        params.put("uidCode", "6gyNzsOWizE");
        System.out.println(getUriRequestContent("http://www.tudou.com/uis/userInfo.action", true, params, "UTF-8"));
    }
}

