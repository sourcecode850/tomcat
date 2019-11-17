package tomcat.learn.practice.xml.jaxb;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * field是成员变量
 * property是属性
 * 含有get、set方法（不写set方法则是只读）并且是私有的才叫属性。
 * 没有get、set方法的是成员变量，成员变量可以用public修饰。
 *
 * @date:2019/11/17 10:54
 **/
// 这个注解好像可以不用加
@XmlRootElement
// 只有 FIELD 才能被转换成 xml 中的标签；不加这个会报错：类的两个属性具有相同名称；
// 会出现version（id）的属性和version（id）的field，导致出现两次version或者两次id
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "books", namespace = "http://www.tomcat.skills.study/xml/books")
@Data
public class Books {
    // XmlElement可以指定Book所在xml中的命名空间；
    // 这里的name不能忘记，否则list是空的
    @XmlElement(name = "book", namespace = "http://www.tomcat.skills.study/xml/books")
    private List<Book> bookList;
    @XmlAttribute(name = "id", required = true)
    private String id;
    @XmlAttribute(name = "version", required = true)
    private String version;
}
