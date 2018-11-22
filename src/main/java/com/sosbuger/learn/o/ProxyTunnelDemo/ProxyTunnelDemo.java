package com.sosbuger.learn.o.ProxyTunnelDemo;

import com.sosbuger.custom.Custom;
import org.apache.http.Consts;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.ProxyClient;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.net.Socket;

/**
 * 隧道代理
 */
public class ProxyTunnelDemo {

    public static void main(String[] args) throws IOException, HttpException {
        ProxyClient proxyClient = new ProxyClient();
        HttpHost target = new HttpHost("www.baidu.com",80,"https");
        HttpHost proxy = new HttpHost("172.18.254.33", 8001);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("user", "pwd");
        Socket socket = proxyClient.tunnel(proxy, target, credentials);
        Writer out = new OutputStreamWriter(socket.getOutputStream(), HTTP.DEF_CONTENT_CHARSET);
        out.write("GET / HTTP/1.1\r\n");
        out.write("Host: " + target.toHostString() + "\r\n");
        out.write("Agent: whatever\r\n");
        out.write("Connection: close\r\n");
        out.write("\r\n");
        out.flush();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), Consts.UTF_8));
        String line = null;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }
}
