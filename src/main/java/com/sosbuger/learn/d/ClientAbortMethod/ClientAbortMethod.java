package com.sosbuger.learn.d.ClientAbortMethod;

import com.sosbuger.custom.Custom;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sun.plugin2.message.CustomSecurityManagerAckMessage;

import java.io.IOException;

/**
 * 在网络请求完成前终止请求
 */
public class ClientAbortMethod {

    public static void main(String[] args) throws IOException {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(Custom.BAIDU);
            System.out.println("----------------------------------------");
            System.out.println("Executing request " + httpget.getURI());
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                System.err.println(response.getStatusLine());
                httpget.abort();
            }
        }
    }
}
