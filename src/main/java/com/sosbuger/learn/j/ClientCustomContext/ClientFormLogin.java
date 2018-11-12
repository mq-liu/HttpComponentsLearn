package com.sosbuger.learn.j.ClientCustomContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ClientFormLogin {

    public static void main(String[] args) {

        CookieStore cookieStore = new BasicCookieStore();

        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build()) {
            HttpGet httpGet = new HttpGet("http://www.xilinjie.com/user/login");
            CloseableHttpResponse response1 = httpClient.execute(httpGet);
            HttpEntity entity1 = response1.getEntity();
            String content1 = EntityUtils.toString(entity1, Consts.UTF_8);
            Document doc = Jsoup.parse(content1);
            Elements inputs = doc.select("input");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            for (Element element : inputs) {
                String value ;
                String name = element.attr("name");
                switch (name) {
                    case "name":
                        value = "***********";
                        break;
                    case "password":
                        value = "***********";
                        break;
                    default:
                        value = element.val();
                        break;
                }
                if(StringUtils.isNotEmpty(name)){
                    NameValuePair nameValuePair = new BasicNameValuePair(name, value);
                    nameValuePairs.add(nameValuePair);
                    System.out.println(name +":"+element.val());
                }
            }
            NameValuePair[] nameValuePairs1 = new NameValuePair[nameValuePairs.size()];
            nameValuePairs.toArray(nameValuePairs1);
            HttpUriRequest request = RequestBuilder.post()
                    .setUri("http://www.xilinjie.com/user/login")
                    .addParameters(nameValuePairs1)
                    .build();
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                List<Cookie> cookies = cookieStore.getCookies();
                for (Cookie cookie : cookies) {
                    System.out.println(cookie);
                }
                String content = EntityUtils.toString(entity, Consts.UTF_8);
                // System.out.println(content);
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
