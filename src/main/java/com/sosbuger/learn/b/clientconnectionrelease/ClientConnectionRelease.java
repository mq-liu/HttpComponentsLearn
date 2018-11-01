package com.sosbuger.learn.b.clientconnectionrelease;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author admin
 * @date 2018年10月29日
 */
public class ClientConnectionRelease {

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet("http://www.baidu.cn/");
            System.out.println("Executing request " + httpget.getRequestLine()); // 请求头
            CloseableHttpResponse response = httpclient.execute(httpget);

            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());

                // Get hold of the response entity
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream instream = entity.getContent();
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        byte[] buff = new byte[1024];
                        int len = -1;
                        while((len = instream.read(buff))!= -1){
                            stringBuilder.append(new String(buff, 0, len));
                        }
                        System.out.println(stringBuilder.toString());
                    } catch (IOException e) {
                        // In case of an IOException the connection will be released
                        // back to the connection manager automatically
                        throw e;
                    } finally {
                        // Closing the input stream will trigger connection release
                        instream.close();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                response.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.close();
        }
    }

}
