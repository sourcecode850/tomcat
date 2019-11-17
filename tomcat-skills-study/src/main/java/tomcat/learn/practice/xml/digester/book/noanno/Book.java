package tomcat.learn.practice.xml.digester.book.noanno;

import lombok.Data;

/**
 * 创建对象用来装载XML文档中的内容：
 * 5 Book.java，用来存放books/book标签中的内容
 */
@Data
public class Book {
    private Author author;
    private Byname byname;
    private String creationDate;
    private String literaryStyle;
    private String name;

    public void addByname(Byname byname) {
        this.byname = byname;
    }

    public void addAuthor(Author author) {
        this.author = author;
    }
}
