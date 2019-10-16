package com.study.source.utils;

import java.io.*;

/**
 * @date:2019/10/15 22:49
 **/
public class FileUtil {
    public static boolean writeFile(InputStream inputStream, OutputStream outputStream) {
        boolean success = false;
        BufferedInputStream bufferedInputStream;
        BufferedOutputStream bufferedOutputStream;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(HttpUtil.getHttpResponseContext200("").getBytes());

            int count = 0;
            while (count == 0) {
                count = inputStream.available();
            }

            int fileSize = inputStream.available();
            long written = 0;
            int batSize = 1024;
            byte[] bytes = new byte[batSize];
            while (written < batSize) {
                if (written + batSize > fileSize) {
                    batSize = (int) (fileSize - written);
                    bytes = new byte[batSize];
                }
                bufferedInputStream.read(bytes);
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.flush();
                written += batSize;
            }
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    public static boolean writeFile(File file, OutputStream outputStream) throws FileNotFoundException {
        return writeFile(new FileInputStream(file), outputStream);
    }

    public static String getResoucePath(String path) {
        String resouce = FileUtil.class.getResource("/").getPath();
        return resouce + "\\" + path;
    }
}
