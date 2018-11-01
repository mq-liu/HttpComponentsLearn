package com.sosbuger.learn.b.clientconnectionrelease;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author admin
 * @date 2018年10月29日
 */
public class ClientConnectionRelease {

	public static void main(String[] args) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet("http://www.json.cn/");
			System.out.println("Executing request " + httpget.getRequestLine()); // 请求头
			CloseableHttpResponse response = httpclient.execute(httpget);

			try {
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());

				// Get hold of the response entity
				HttpEntity entity = response.getEntity();



			} catch (Exception e) {
				e.printStackTrace();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
