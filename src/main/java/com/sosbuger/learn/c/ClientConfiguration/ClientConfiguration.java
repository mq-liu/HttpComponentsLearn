package com.sosbuger.learn.c.ClientConfiguration;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.*;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.*;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

/**
 * DefaultHttpResponseParserFactory
 * DefaultHttpRequestWriterFactory
 * ManagedHttpClientConnectionFactory
 * SSLConnectionSocketFactory
 * SystemDefaultDnsResolver
 * PoolingHttpClientConnectionManager
 * MessageConstraints
 * RequestConfig
 *
 */
public class ClientConfiguration {

    public static void main(String[] args) throws IOException {

        // 自定义响应解析工厂
        HttpMessageParserFactory<HttpResponse> responseParserFacroty = new DefaultHttpResponseParserFactory() {
            @Override
            public HttpMessageParser<HttpResponse> create(SessionInputBuffer buffer, MessageConstraints constraints) {
                LineParser lineParser = new BasicLineParser() {

                    @Override
                    public Header parseHeader(CharArrayBuffer buffer) throws ParseException {
                        try {
                            return super.parseHeader(buffer);
                        } catch (ParseException e) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }
                };

                return new DefaultHttpResponseParser(buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {
                    @Override
                    protected boolean reject(CharArrayBuffer line, int count) {
                        // try to ignore all garbage preceding a status line infinitely
                        return false;
                    }
                };
            }
        };
        // 自定义请求写入工厂
        HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
        // 自定义链接管理工厂
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory = new ManagedHttpClientConnectionFactory(requestWriterFactory, responseParserFacroty);
        // 安全套接字(解密方式)
        SSLContext sslContext = SSLContexts.createDefault();

        // 创建自定义协议连接套接字工厂的注册表。
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslContext))
                .build();

        // 使用自定义的DNS域名解析
        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {
            @Override
            public InetAddress[] resolve(String host) throws UnknownHostException {
                if ("myhost".equals(host)) {
                    return new InetAddress[]{InetAddress.getByAddress(new byte[]{127, 0, 0, 1})};
                } else {
                    return super.resolve(host);
                }
            }
        };

        // 使用自定义配置创建连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory, dnsResolver);
        // 创建socket配置
        SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        // 使用自定义的socket配置
        connManager.setDefaultSocketConfig(socketConfig);
        // 部分站点各自的socket设置
        connManager.setSocketConfig(new HttpHost("somehost", 80), socketConfig);
        connManager.setValidateAfterInactivity(1000);
        // 创建消息约束
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();
        // 创建连接配置
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();
        // 自定义连接配置
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setConnectionConfig(new HttpHost("somehost", 80), ConnectionConfig.DEFAULT);
        // 配置每个连接的最大路由
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(10);
        connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 10);
        // 设置自定义的cookie存储
        CookieStore cookieStore = new BasicCookieStore();
        // 如需必要, 使用自定义的证书提供方
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        // 创建全局请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        // 使用自定义或者默认配置创建httpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultCookieStore(cookieStore)
                .setDefaultCredentialsProvider(credentialsProvider)
                // .setProxy(new HttpHost("proxyHost", 8080))
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();

        try {
            HttpGet httpGet = new HttpGet("http://members.3322.org/dyndns/getip");
            // 请求配置可以被覆盖, 优先使用client级别的配置
            RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                    .setSocketTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .setConnectTimeout(5000)
                    // .setProxy(new HttpHost("otherProxyHost", 8080))
                    .build();
            httpGet.setConfig(requestConfig);

            // 执行环境可以在本地自定义
            HttpClientContext context = HttpClientContext.create();
            context.setCookieStore(cookieStore);
            context.setCredentialsProvider(credentialsProvider);
            System.err.println("executing request " + httpGet.getURI());
            CloseableHttpResponse response = httpClient.execute(httpGet, context);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
                System.out.println("----------------------------------------");

                context.getRequest();
                context.getHttpRoute();
                context.getTargetAuthState();
                context.getTargetAuthState();
                context.getCookieOrigin();
                context.getCookieSpec();
                context.getUserToken();

            } finally {
                response.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }

    }

}
