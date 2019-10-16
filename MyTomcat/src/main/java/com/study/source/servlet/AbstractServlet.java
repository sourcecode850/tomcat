package com.study.source.servlet;

import lombok.extern.slf4j.Slf4j;

/**
 * @date:2019/10/15 23:17
 **/
@Slf4j
public abstract class AbstractServlet {

    public abstract void doGet(Request request, Response response);

    public abstract void doPost(Request request, Response response);

    public void doService(Request request, Response response) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            doPost(request, response);
        } else {
            log.error("暂不支持的请求方式！");
        }

    }

}
