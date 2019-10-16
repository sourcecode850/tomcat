package com.study.source.utils;

/**
 * 以Http协议标准规范，构造响应信息体，相当于response.setContentType("text/html")
 *
 * @date:2019/10/15 22:50
 **/
public class HttpUtil {
    //这里如果不按照标准的http协议写的话，一般的高级浏览器是没法进行处理的，只有低版本的IE才
    //可以直接使用response.getOutputStream().write(bytes);
    //这里的content就是index.html文件整个内容；加上协议头，浏览器就能正常解析了
    public static String getHttpResponseContext(int code, String content, String errorMsg) {
        if (code == 200) {
            return "HTTP/1.1 200 OK \n" +
                    "Content-Type: text/html\n" +
                    "\r\n" + content;
        } else if (code == 500) {
            return "HTTP/1.1 500" + " \n" +
                    "Content-Type: text/html\n" +
                    "\r\n" + errorMsg;
        }
        return "HTTP/1.1 404 NOT Found \n" +
                "Content-Type: text/html\n" +
                "\r\n" +
                "<h1>404 Not Found</h1>";
    }

    public static String getHttpResponseContext200(String content) {
        return getHttpResponseContext(200, content, "");
    }

    public static String getHttpResponseContext404() {
        return getHttpResponseContext(404, "", "");
    }

    public static String getHttpResponseContext500(Throwable cause) {
        StringBuffer sb = new StringBuffer(cause.toString());
        StackTraceElement[] stes = null;
        if ((stes = cause.getStackTrace()) != null) {
            for (StackTraceElement ste : stes) {
                // sb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + ste);
                sb.append("\n\t" + ste);
            }
        }
        return getHttpResponseContext(500, "", "<h1>Internal Server Error:</h1>".concat("<div style='white-space:pre-wrap;'>" + sb.toString() + "</div>"));
    }
}
