package com.study.source;

import com.study.source.config.ServletConfig;
import com.study.source.config.ServletConfigMapping;
import com.study.source.servlet.AbstractServlet;
import com.study.source.servlet.Request;
import com.study.source.servlet.Response;
import com.study.source.utils.HttpUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @date:2019/10/14 22:30
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LuBanTomcat {

    private int port = 8080;

    public LuBanTomcat(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        LuBanTomcat luBanTomcat = new LuBanTomcat(8080);
        luBanTomcat.start();
    }

    public void start() {
        initServlet();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("my tomcat start on " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("接收到请求啦====>" + UUID.randomUUID());
                Request request = new Request(socket.getInputStream());
                Response response = new Response(socket.getOutputStream());
                if ("/".equalsIgnoreCase(request.getUrl())) {
                    response.write(HttpUtil.getHttpResponseContext404());
                } else if (stringClassMap.get(request.getUrl()) == null) {
                    response.writeHtml(request.getUrl());
                } else {
                    dispatch(request, response);
                }
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Class<AbstractServlet>> stringClassMap = new HashMap<>();

    public void initServlet() {
        for (ServletConfig servletConfig : ServletConfigMapping.getConfigs()) {
            try {
                stringClassMap.put(servletConfig.getUrlMapping(),
                        (Class<AbstractServlet>) Class.forName(servletConfig.getClazz()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //分发请求
    public void dispatch(Request request, Response response) {
        Class<AbstractServlet> servletClass = stringClassMap.get(request.getUrl());
        if (servletClass != null) {
            AbstractServlet servlet = null;
            try {
                servlet = servletClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            servlet.doService(request, response);
        }
    }
}
