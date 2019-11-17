package tomcat.learn.practice.xml.digester.book.noanno;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建对象用来装载XML文档中的内容：
 * 4 Author.java，用来存放books/book/author/name标签集合
 */
@Data
public class Author {

    private List<AuthorName> authorNames;

    public void addAuthorName(AuthorName name) {
        if (name == null) {
            return;
        }
        if (this.authorNames == null) {
            this.authorNames = new ArrayList<>();
        }
        this.authorNames.add(name);
    }
}