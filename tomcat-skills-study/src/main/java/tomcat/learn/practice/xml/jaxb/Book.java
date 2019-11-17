package tomcat.learn.practice.xml.jaxb;

import lombok.Data;

import javax.xml.bind.annotation.*;

/**
 * @date:2019/11/17 10:54
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "book", namespace = "http://www.tomcat.skills.study/xml/books")
@Data
public class Book {
    @XmlElement(name = "author", namespace = "http://www.tomcat.skills.study/xml/books")
    private Author author;
    @XmlElement(name = "byname", namespace = "http://www.tomcat.skills.study/xml/books")
    private Byname byname;
    //  @XmlAttribute(name = "creationDate")
    // 注意这里creationDate是简单元素而不是attribute；简单元素可以直接使用java基本类型接收了
    // 注意这里creationDate是简单元素而不是attribute；简单元素可以直接使用java基本类型接收了
    // 注意这里creationDate是简单元素而不是attribute；简单元素可以直接使用java基本类型接收了
    @XmlElement(name = "creationDate", namespace = "http://www.tomcat.skills.study/xml/books")
    private String creationDate;
    @XmlElement(name = "literaryStyle", namespace = "http://www.tomcat.skills.study/xml/books")
    private String literaryStyle;
    @XmlAttribute(name = "bookName")
    private String bookName;
}
