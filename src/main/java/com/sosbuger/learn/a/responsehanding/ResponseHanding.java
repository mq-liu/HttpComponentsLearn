package com.sosbuger.learn.a.responsehanding;

import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 官方推荐处理响应的方法
 *
 * @author admin
 * @date 2018年10月29日
 * 
 */
public class ResponseHanding {

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();

		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
										 //http://edu.csii.com.cn/hls/1269/stream/shd/t23QlK790S3cKOinuNoeQqZI35BAjrcX.m3u8
			HttpGet httpget = new HttpGet("https://www.baidu.com/");
			System.out.println("Executing request " + httpget.getRequestLine()); // 请求头

			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					System.out.println("response status: " + status);
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity, Charset.forName("UTF-8")) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};

			String responseBody = httpClient.execute(httpget, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);

		} catch (Exception e) {
			e.printStackTrace();
			httpClient.close();
		}

		long end = System.currentTimeMillis();
		System.err.println(end - start);
	}

}
