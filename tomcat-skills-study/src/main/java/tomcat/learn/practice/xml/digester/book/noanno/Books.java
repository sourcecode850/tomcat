package tomcat.learn.practice.xml.digester.book.noanno;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建对象用来装载XML文档中的内容：
 * 6 Books.java，用来存放books/book标签集。
 */
@Data
public class Books {
    private List<Book> bookList;
    private String id;
    private String version;

    public void addBooks(Book book) {
        if (book == null) {
            return;
        }
        if (this.bookList == null) {
            this.bookList = new ArrayList<>();
        }
        this.bookList.add(book);
    }
}