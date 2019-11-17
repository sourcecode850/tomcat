package tomcat.learn.practice.xml.jaxb;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;

/**
 * @date:2019/11/16 22:42
 **/

public class XmlToBeanJaxb {

    private static SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    private static Schema schema;

    static {
        try {
            // 注意这里是xsd而不是xml，之前写错了，找了好久错误
            // s4s-elt-schema-ns: 元素 'books' 的名称空间必须来自方案名称空间 'http://www.w3.org/2001/XMLSchema'
            URL url = Thread.currentThread().getClass().
                    getResource("/xml/books.xsd");
            schema = schemaFactory.newSchema(url);
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testXmlToBooks() throws XMLStreamException {
        XMLStreamReader reader =
                new XMLStreamReaderImpl(Thread.currentThread().getClass().getResourceAsStream("/xml/books.xml"),
                        new PropertyManager(1));
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Books.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);
            JAXBElement<Books> jaxbElement = unmarshaller.unmarshal(reader, Books.class);
            Books books = jaxbElement.getValue();
            System.out.println(books);
            System.out.println(JSON.toJSONString(books));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
