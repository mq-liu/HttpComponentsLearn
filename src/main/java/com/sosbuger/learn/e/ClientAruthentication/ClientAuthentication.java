package com.sosbuger.learn.e.ClientAruthentication;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * http auth验证
 */
public class ClientAuthentication {

    public static void main(String[] args) throws IOException {

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope("httpbin.org", 80), new UsernamePasswordCredentials("user", "passwd"));

        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build()) {
            HttpGet httpGet = new HttpGet("http://httpbin.org/basic-auth/user/passwd");
            try(CloseableHttpResponse response = httpClient.execute(httpGet)){
                System.out.println("Executing request " + httpGet.getRequestLine());
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }
}
