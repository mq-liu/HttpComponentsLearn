package com.sosbuger.learn.m.ClientPreemptiveBasicAuthentication;

import com.sosbuger.custom.Custom;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ClientPreemptiveBasicAuthentication {

    public static void main(String[] args) {
        HttpHost target = new HttpHost("httpbin.org", 80, "http");
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials("user", "passwd"));
        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build()) {
            AuthCache authCache = new BasicAuthCache();
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(target,basicAuth);
            // Add AuthCache to the execution context
            HttpClientContext localContext = HttpClientContext.create();
             localContext.setAuthCache(authCache);
            HttpGet httpget = new HttpGet("http://httpbin.org/hidden-basic-auth/user/passwd");
            System.out.println("Executing request " + httpget.getRequestLine() + " to target " + target);
            for (int i = 0; i < 3; i++) {
                try (CloseableHttpResponse response = httpClient.execute(target, httpget, localContext)){
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    System.out.println(EntityUtils.toString(response.getEntity()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
