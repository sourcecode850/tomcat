package com.study.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * @date:2019/10/14 22:30
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LuBanTomcat {

    private int port = 8080;

    public static void main(String[] args) throws IOException {
        LuBanTomcat luBanTomcat = new LuBanTomcat(8080);
        luBanTomcat.start();
    }

    public void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("my tomcat start on " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("接收到请求啦====>" + UUID.randomUUID());

                InputStream inputStream = socket.getInputStream();

                int count = 0;
                while (count == 0) {
                    count = inputStream.available();
                }

                byte[] bytes = new byte[inputStream.available()];
                System.out.println(new String(bytes));

                socket.getOutputStream().write("hello,mytomcat".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
