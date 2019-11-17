package tomcat.learn.practice.xml.digester.book.anno;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;

public class Test2 {

    public static Books readBooks(File xmlPath, Class<?> XmlClazz) throws IOException, SAXException {
        Digester digester = getLoader(XmlClazz).newDigester();
        return digester.parse(xmlPath);
    }

    public static DigesterLoader getLoader(final Class<?> XmlClazz) {
        return newLoader(new FromAnnotationsRuleModule() {
            @Override
            protected void configureRules() {
                bindRulesFrom(XmlClazz);
            }
        });
    }

    public static void main(String[] args) throws IOException, SAXException {
        File xmlFile = new File(Thread.currentThread().getClass().getResource("/xml/books.xml").getPath());
        Books books = readBooks(xmlFile, Books.class);
        System.out.println(books);
        System.out.println(JSONObject.toJSONString(books));
    }

}