package tomcat.learn.practice.xml.jaxb;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @date:2019/11/17 20:44
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "author", namespace = "http://www.tomcat.skills.study/xml/books")
@Data
public class Author {
    @XmlElement(name = "authorName", namespace = "http://www.tomcat.skills.study/xml/books")
    private List<String> authorNames;
}
