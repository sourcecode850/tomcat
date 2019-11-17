package tomcat.learn.practice.xml.jaxb;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @date:2019/11/17 20:54
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "byName", namespace = "http://www.tomcat.skills.study/xml/books")
@Data
public class Byname {
    @XmlElement(name = "bynameName", namespace = "http://www.tomcat.skills.study/xml/books")
    private List<String> bynames;
}
