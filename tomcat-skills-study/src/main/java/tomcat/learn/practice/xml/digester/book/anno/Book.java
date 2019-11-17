package tomcat.learn.practice.xml.digester.book.anno;

import lombok.Data;
import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.digester3.annotations.rules.SetProperty;

/**
 * Digester3根据注解来映射Java对象和XML文档，在方式一的基础上的对象添加注解即可，具体如下：
 * 5 Book.java，用来存放books/book标签中的内容
 */
//books/book标签和Book对象映射
@ObjectCreate(pattern = "books/book")
@Data
public class Book {

    private Author author;

    private Byname byname;

    //books/book/creationDate标签内容和Book的creationDate属性映射
    @BeanPropertySetter(pattern = "books/book/creationDate")
    private String creationDate;
    //books/book/literaryStyle标签内容和Book的literaryStyle属性映射
    @BeanPropertySetter(pattern = "books/book/literaryStyle")
    private String literaryStyle;
    //将/books/book标签的所有属性映射到Book对象的属性上，在这里映射的是/books/book标签的name属性。
    @SetProperty(pattern = "books/book")
    private String name;

    //将books/book/byname标签的内容对象添加到Book对象中
    @SetNext
    public void addByname(Byname byname) {
        this.byname = byname;
    }

    //将books/book/author标签的内容对象添加到Book对象中
    @SetNext
    public void addAuthor(Author author) {
        this.author = author;
    }
}