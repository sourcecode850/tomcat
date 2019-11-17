package tomcat.learn.practice.xml.digester.book.anno;

import lombok.Data;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

import java.util.ArrayList;
import java.util.List;

/**
 * Digester3根据注解来映射Java对象和XML文档，在方式一的基础上的对象添加注解即可，具体如下：
 * 4 Author.java，用来存放books/book/author/name标签中的内容
 */
//books/book/author标签和Author对象映射
@ObjectCreate(pattern = "books/book/author")
@Data
public class Author {
    private List<AuthorName> names;

    //将books/book/author/name标签的内容对象添加到Author对象中
    @SetNext
    public void addAuthorName(AuthorName name) {
        if (name == null) {
            return;
        }
        if (this.names == null) {
            this.names = new ArrayList<>();
        }
        this.names.add(name);
    }
}