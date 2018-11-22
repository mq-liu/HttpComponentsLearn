package com.sosbuger.learn.l.ClientCustomSSL;

import com.sosbuger.custom.Custom;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;

/**
 * 自定义安全的https连接
 */
public class ClientCustomSSL {

    public static void main(String[] args) throws Exception {
        // 信任自签证书
        SSLContext sslContext = SSLContexts.custom()
//                .loadTrustMaterial(
//                        new File("my.keystore"),
//                        "nopasswd".toCharArray(),
//                        new TrustSelfSignedStrategy())
                .build();

        // 只允许 TLSv1
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build()) {
            HttpGet httpget = new HttpGet(Custom.GETIP);
            System.out.println("Executing request " + httpget.getRequestLine());
            try (CloseableHttpResponse response = httpclient.execute(httpget)){
                HttpEntity entity = response.getEntity();
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                String content = EntityUtils.toString(entity);
                System.out.println(content);
                EntityUtils.consume(entity);
            }
        }
    }

}
