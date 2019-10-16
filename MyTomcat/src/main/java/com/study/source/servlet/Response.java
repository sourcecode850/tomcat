package com.study.source.servlet;

import com.study.source.utils.FileUtil;
import com.study.source.utils.HttpUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @date:2019/10/15 22:46
 **/
@Data
@Slf4j
public class Response {
    private OutputStream outputStream;

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeHtml(String path) throws Exception {
        String resoucePath = FileUtil.getResoucePath(path);
        File file = new File(resoucePath);
        if (file.exists()) {
            FileUtil.writeFile(file, outputStream);
        } else {
            write(HttpUtil.getHttpResponseContext404());
        }
    }

    public void write(String content) {
        try {
            outputStream.write(content.getBytes("utf-8"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
