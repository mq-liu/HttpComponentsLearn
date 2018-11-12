package com.sosbuger.learn.i.ClientCustomContext;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class ClientCustomContext {

    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            // 创建一个存储cookie的本地实例
            CookieStore cookieStore = new BasicCookieStore();

            // 创建一个http请求上下文实例
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setCookieStore(cookieStore);
            HttpGet httpget = new HttpGet("https://www.baidu.com/");
            System.out.println("Executing request " + httpget.getRequestLine());

            try (CloseableHttpResponse response = httpClient.execute(httpget, localContext)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                List<Cookie> cookies = cookieStore.getCookies();
                for (Cookie cookie : cookies) {
                    System.out.println(cookie);
                }
                EntityUtils.consume(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
