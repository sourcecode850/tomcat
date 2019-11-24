package tomcat.learn.practice.socket;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 实现http协议
 *
 * @author niepu
 * @since 2019-11-21
 */
public class MyHttpServer {

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5,
            20, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel，监听8080端口
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(8080));
        // 设置为非阻塞模式
        ssc.configureBlocking(false);
        // 为ssc注册选择器
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        // 创建处理器
        while (true) {
            // 等待请求，每次等待阻塞3s，超过3s后线程继续向下运行，如果传入0或者不传参将一直阻塞
            if (selector.select(3000) == 0) {
                continue;
            }
            // 获取待处理的SelectionKey
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                // 先删SelectionKey迭代器中移除当前所使用的key
                keyIterator.remove();

                // 交给线程池处理SelectionKey
                executor.execute(new HttpHandler(key));

            }
        }
    }


    private static class HttpHandler implements Runnable {
        private int bufferSize = 1024;
        private String localCharset = "UTF-8";
        private SelectionKey key;

        public HttpHandler(SelectionKey key) {
            this.key = key;
        }

        public void handlerAccept() throws IOException {
            SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
            // 这里经常出现null值的clientChannel，所以加个空判断吧
            if (!Objects.isNull(clientChannel)) {
                clientChannel.configureBlocking(false);
                clientChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
            }
        }

        // 714 722 725 731 780 806 859 918
        public void handleRead() throws IOException {
            // 获取channel
            SocketChannel sc = (SocketChannel) key.channel();
            // 获取buffer并重置
            ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
            byteBuffer.clear();
            // 没有读到内容则关闭
            if (sc.read(byteBuffer) == -1) {
                // sc.close()改成注册到selector上的读事件
                sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
            } else {
                // 接收请求数据
                byteBuffer.flip();
                String receivedString = Charset.forName(localCharset).newDecoder().decode(byteBuffer).toString();

                // 不知道为啥，一次请求，这里会收到多次receivedString，除了第一次receivedString，其他好像都是空的，导致下面处理receivedString报空指针
                if (StringUtils.isBlank(receivedString)) {
                    sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
                    return;
                }

                // 控制台打印请求报文头
                String[] requestMessage = receivedString.split("\r\n");
                for (String s : requestMessage) {
                    System.out.println(s);
                    // 遇到空行说明报文头已经打完了
                    if (s.isEmpty()) {
                        break;
                    }
                }
                // 控制台打印首行信息
                String[] firstLine = requestMessage[0].split(" ");
                System.out.println();
                System.out.println("Method:\t" + firstLine[0]);
                System.out.println("url:\t" + firstLine[1]);
                System.out.println("HTTP Version:\t" + firstLine[2]);
                System.out.println();

                // 返回客户端
                StringBuilder sendString = new StringBuilder();
                // \n是换行,英文是New line。\r是回车,英文是Carriage return
                sendString.append("HTTP/1.1 200 OK\r\n");//响应报文首行，200表示成功
                sendString.append("Content-Type:text/html;charset=" + localCharset + "\r\n");
                sendString.append("\r\n");// 报文结束后加一个空行

                sendString.append("<html><head><title>显示报文</title></head><body>");
                sendString.append("接收到请求报文是:<br/>");
                for (String s : requestMessage) {
                    sendString.append(s + "<br/>");
                }
                // 打印socketChannel
                sendString.append("<hr/>" + sc + "<br/><hr/>");
                sendString.append("</body></html>");

                byteBuffer = ByteBuffer.wrap(sendString.toString().getBytes(localCharset));
                sc.write(byteBuffer);

                // 这里直接关闭socket肯定达不到复用的目的
                // sc.close()改成注册到selector的读事件
                sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
            }
        }

        @Override
        public void run() {
            try {
                // 接收到连接请求
                if (key.isAcceptable()) {
                    handlerAccept();
                }
                // 读数据
                if (key.isReadable()) {
                    handleRead();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
