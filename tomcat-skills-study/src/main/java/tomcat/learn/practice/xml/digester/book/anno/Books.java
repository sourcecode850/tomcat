package tomcat.learn.practice.xml.digester.book.anno;

import lombok.Data;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.digester3.annotations.rules.SetProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Digester3根据注解来映射Java对象和XML文档，在方式一的基础上的对象添加注解即可，具体如下：
 * 6 Books.java，用来存放books/book标签集
 */
//books标签和Books对象映射
@ObjectCreate(pattern = "books")
@Data
public class Books {

    private List<Book> bookList;
    @SetProperty(pattern = "books")
    private String id;
    @SetProperty(pattern = "books")
    private String version;

    //将books/book标签的内容对象添加到Books对象中
    @SetNext
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