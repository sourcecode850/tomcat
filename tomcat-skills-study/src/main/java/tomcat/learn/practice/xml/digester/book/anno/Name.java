package tomcat.learn.practice.xml.digester.book.anno;

import lombok.Data;
import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;

/**
 * Digester3根据注解来映射Java对象和XML文档，在方式一的基础上的对象添加注解即可，具体如下：
 * 1 Name.java，用来存放books/book/byname/name标签中的内容
 */

//books/book/byname/name标签和Name对象映射
@ObjectCreate(pattern = "books/book/byname/bynameName")
@Data
public class Name {
    //books/book/byname/name标签内容和AuthorName的name属性映射
    @BeanPropertySetter(pattern = "books/book/byname/bynameName")
    private String name;
}