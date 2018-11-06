package com.sosbuger.learn.g.ClientProxyAuthentication;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 使用需要验证的代理请求
 */
public class ClientProxyAuthentication {

    public static void main(String[] args) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(new HttpHost("127.0.0.1", 8001)),
                new UsernamePasswordCredentials("root", "root"));
        credentialsProvider.setCredentials(
                new AuthScope(new HttpHost("httpbin.org", 80)),
                new UsernamePasswordCredentials("user", "passwd"));

        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build()) {
            HttpHost target = new HttpHost("httpbin.org", 80, "http");
            HttpHost proxy = new HttpHost("127.0.0.1", 8001);
            RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
            HttpGet httpget = new HttpGet("/basic-auth/user/passwd");
            httpget.setConfig(requestConfig);
            try(CloseableHttpResponse response = httpClient.execute(target, httpget)){
                System.out.println("Executing request " + httpget.getRequestLine() + " to " + target + " via " + proxy);
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
