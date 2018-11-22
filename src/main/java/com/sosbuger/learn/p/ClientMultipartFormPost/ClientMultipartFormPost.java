package com.sosbuger.learn.p.ClientMultipartFormPost;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * 模拟多部分表单提交
 */
public class ClientMultipartFormPost {

    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://host:port/XXX");
            FileBody fileBody = new FileBody(new File(""));
            StringBody stringBody = new StringBody("", ContentType.TEXT_PLAIN);
            HttpEntity httpEntity = MultipartEntityBuilder.create()
                    .addPart("file", fileBody)
                    .addPart("kw", stringBody)
                    .build();
            httpPost.setEntity(httpEntity);
            System.out.println("executing request " + httpPost.getRequestLine());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)){
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
