package com.sosbuger.learn.f.ClientExecuteProxy;

import com.sosbuger.custom.Custom;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 通过代理请求
 */
public class ClientExecuteProxy {

    public static void main(String[] args) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpHost target = new HttpHost("members.3322.org", 80, "http");
            HttpHost proxy = new HttpHost("127.0.0.1", 8001, "http");

            RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
            HttpGet httpGet = new HttpGet("/dyndns/getip/");
            httpGet.setConfig(requestConfig);

            try (CloseableHttpResponse response = httpClient.execute(target, httpGet)) {
                System.out.println("Executing request " + httpGet.getRequestLine() + " to " + target + " via " + proxy);
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
