package com.sosbuger.learn.h.ClientChunkEncodedPost;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 客户端编码发送post请求
 */
public class ClientChunkEncodedPost {

    public static void main(String[] args) {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://httpbin.org/post");
            File file = new File("C:\\Users\\admin\\Desktop\\request.txt");
            InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1, ContentType.APPLICATION_OCTET_STREAM);
            reqEntity.setChunked(true);
            httpPost.setEntity(reqEntity);
            System.out.println("Executing request: " + httpPost.getRequestLine());
            // 推荐使用FileEntity
            // FileEntity entity = new FileEntity(file, "binary/octet-stream");
            System.out.println("Executing request: " + httpPost.getRequestLine());
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
