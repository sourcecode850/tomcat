package tomcat.learn.practice.xml.digester.book.anno;

import lombok.Data;
import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;

/**
 * Digester3根据注解来映射Java对象和XML文档，在方式一的基础上的对象添加注解即可，具体如下：
 * 3 AuthorName.java，用来存放books/book/author/name标签中的内容
 */
//books/book/author/anme标签和AuthorName对象映射
@ObjectCreate(pattern = "books/book/author/authorName")
@Data
public class AuthorName {
    @BeanPropertySetter(pattern = "books/book/author/authorName")
    private String name;
}