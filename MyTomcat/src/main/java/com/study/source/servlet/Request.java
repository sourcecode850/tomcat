package com.study.source.servlet;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @date:2019/10/15 22:36
 **/
@Data
public class Request {

    private String url;
    private String method;
    private InputStream inputStream;

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }

        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        extractFields(new String(bytes));
    }

    private void extractFields(String content) {
        if (StringUtils.isNoneEmpty()) {
            System.out.println("empty");
        } else {
            String firstLine = content.split("\\n")[0];
            String[] ss = firstLine.split("\\s");
            setUrl(ss[1]);
            setMethod(ss[0]);
        }
    }
}
