package tomcat.learn.practice.xml.digester.book.noanno;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建对象用来装载XML文档中的内容：
 * 2 Byname.java，用来存放books/book/byname/name标签集合。
 */
@Data
public class Byname {

    private List<BynameName> bynameName;

    public void addByname(BynameName name) {
        if (name == null) {
            return;
        }
        if (this.bynameName == null) {
            this.bynameName = new ArrayList<>();
        }
        this.bynameName.add(name);
    }
}