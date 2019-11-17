package tomcat.learn.practice.xml.digester.book.noanno;

import lombok.Data;

/**
 * 创建对象用来装载XML文档中的内容：
 * 3 AuthorName.java，用来存放books/book/author/name标签中的内容。
 */
@Data
public class AuthorName {
    private String name;
}