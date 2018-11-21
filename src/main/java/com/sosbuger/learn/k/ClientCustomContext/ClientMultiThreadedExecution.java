package com.sosbuger.learn.k.ClientCustomContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 连接池发送http请求
 */
public class ClientMultiThreadedExecution {

    public static void main(String[] args) throws InterruptedException {
        // 多线程必须使用cm
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(500);
        cm.setDefaultMaxPerRoute(10);

        try (CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build()) {

            List<String> urls = Arrays.asList("https://www.baidu.com/", "https://www.sogou.com/", "https://www.qq.com/");
            GetThread[] threads = new GetThread[urls.size()];
            int i = 0;
            for (String url : urls) {
                HttpGet httpget = new HttpGet(url);
                threads[i] = new GetThread(httpClient, httpget, i++);
            }

            // start the threads
            for (GetThread getThread: threads) {
                getThread.start();
            }

            // join the threads
            for (GetThread getThread: threads) {
                getThread.join();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * A thread that performs a GET.
     */
    static class GetThread extends Thread {

        private final CloseableHttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        private final int id;

        GetThread(CloseableHttpClient httpClient, HttpGet httpget, int id) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
            this.id = id;
        }


        @Override
        public void run() {
            try {
                System.out.println(id + " - about to get something from " + httpget.getURI());
                try (CloseableHttpResponse response = httpClient.execute(httpget, context)){
                    System.out.println(id + " - get executed");
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        byte[] bytes = EntityUtils.toByteArray(entity);
                        System.out.println(id + " - " + bytes.length + " bytes read");
                    }
                }
            } catch (Exception e) {
                System.out.println(id + " - error: " + e);
            }
        }

    }

}
