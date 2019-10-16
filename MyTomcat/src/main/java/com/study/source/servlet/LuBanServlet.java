package com.study.source.servlet;

import com.study.source.utils.HttpUtil;

/**
 * @date:2019/10/15 23:20
 **/
public class LuBanServlet extends AbstractServlet {

    @Override
    public void doGet(Request request, Response response) {
        String content = "<h1>this is my first servlet GET</h1>";
        response.write(HttpUtil.getHttpResponseContext200(content));
    }

    @Override
    public void doPost(Request request, Response response) {
        String content = "<h1>this is my first servlet POST</h1>";
        response.write(HttpUtil.getHttpResponseContext200(content));
    }
}
