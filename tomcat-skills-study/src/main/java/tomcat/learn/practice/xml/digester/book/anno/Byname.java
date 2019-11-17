package tomcat.learn.practice.xml.digester.book.anno;

import lombok.Data;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

import java.util.ArrayList;
import java.util.List;

/**
 * Digester3根据注解来映射Java对象和XML文档，在方式一的基础上的对象添加注解即可，具体如下：
 * 2 Byname.java，用来存放books/book/byname/name标签集合
 */
//books/book/byname标签和Byname对象映射
@ObjectCreate(pattern = "books/book/byname")
@Data
public class Byname {

    private List<Name> name;

    //将books/book/byname/name标签的内容对象添加到Byname对象中
    @SetNext
    public void addByname(Name name) {
        if (name == null) {
            return;
        }
        if (this.name == null) {
            this.name = new ArrayList<>();
        }
        this.name.add(name);
    }
}