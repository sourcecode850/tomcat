package my.log;

import org.apache.catalina.valves.AbstractAccessLogValve;

import java.io.CharArrayWriter;

/**
 * 自定义AccessLog，在server.xml中，配置MyAccessLogValve,在解析server.xml时候会注册
 * @date:2019/11/16 12:07
 **/
public class MyAccessLogValve extends AbstractAccessLogValve {

    public MyAccessLogValve(){
        System.out.println("===============MyAccessLogValve被构造=================");
    }

    @Override
    protected void log(CharArrayWriter message) {
        System.out.println("-----------------my.log.MyAccessLogValve-------------");
    }
}
