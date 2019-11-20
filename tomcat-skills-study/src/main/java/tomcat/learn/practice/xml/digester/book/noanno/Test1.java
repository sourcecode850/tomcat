package tomcat.learn.practice.xml.digester.book.noanno;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * 创建对象用来装载XML文档中的内容：
 * 7 Test1.java，生成映射XML文档到Java对象和测试
 */
public class Test1 {

    User user = new User();

    public static Books parseXml(File xmlFile) throws IOException, SAXException {
        Digester digester = new Digester();
        digester.setValidating(false);
        // 跟标签/books和Books对象映射
        digester.addObjectCreate("books", Books.class);

        // 将books标签的属性映射到Bookstore对象的属性上
        digester.addSetProperties("books");

//         将books设置给user，让user做最顶级父类
        digester.addSetNext("books", "setBooks", "tomcat.learn.practice.xml.digester.book.noanno.Books");

        // 标签/books/book和Book对象映射
        digester.addObjectCreate("books/book", Book.class);

        // 将/books/book标签的所有属性映射到Book对象的属性上，在这里映射的是/books/book标签的name属性。
        digester.addSetProperties("books/book");
        // 或者使用下面这种方式代替，digester.addSetProperties("books/book");这种方式要求标签属性名和对象中的字段要保持命名一致才可以映射上。
        // digester.addSetProperties("books/book","name","name");

        // 标签books/book/creationDate，和Book对象的creationDate属性映射
        digester.addBeanPropertySetter("books/book/creationDate", "creationDate");
        // 标签books/book/literaryStyle，和Book对象的literaryStyle属性映射
        digester.addBeanPropertySetter("books/book/literaryStyle", "literaryStyle");

        // 标签books/book/author和Author对象映射
        digester.addObjectCreate("books/book/author", Author.class);

        // 标签books/book/author/name和AuthorName对象映射
        digester.addObjectCreate("books/book/author/authorName", AuthorName.class);
        // 标签books/book/author/name，和AuthorName对象的name属性映射
        digester.addBeanPropertySetter("books/book/author/authorName", "name");

        // 标签books/book/byname和Byname对象映射
        digester.addObjectCreate("books/book/byname", Byname.class);
        // 标签books/book/byname/name和Name对象映射
        digester.addObjectCreate("books/book/byname/bynameName", BynameName.class);
        // 标签books/book/byname/name，和Byname对象的name属性映射
        digester.addBeanPropertySetter("books/book/byname/bynameName", "name");

        // 把Book标签对象添加到Book对象中，需要保证Books对象中有addBooks该方法，用于添加装载XML标签内容后的对象信息
        digester.addSetNext("books/book", "addBooks");
        //把Author标签对象和Byname标签对象添加到Book对象中，需要保证Book对象中有addAuthor和addByname方法，用于添加装载XML标签内容后的对象信息
        digester.addSetNext("books/book/author", "addAuthor");
        digester.addSetNext("books/book/byname", "addByname");

        // 把Name标签对象添加到Byname对象中，需要保证Byname对象中有addByname方法（命名任意，只需要对应上即可），用于添加装载XML标签内容后的对象信息
        digester.addSetNext("books/book/byname/bynameName", "addByname");
        // 把AuthorName标签对象添加到Author对象中，需要保证Author对象中有addAuthorName方法（命名任意，只需要对应上即可），用于添加装载XML标签内容后的对象信息
        digester.addSetNext("books/book/author/authorName", "addAuthorName");

        // 这里push最顶层的User之后，返回的对象就不再是Books了，而是User；所以main方法中打印null
        User user = new User();
        digester.push(user);

        Object obj = digester.parse(xmlFile);
        System.out.println(obj);

        if (obj instanceof Books) {
            return (Books) obj;
        }
        return null;

    }

    public static void main(String[] args) throws IOException, SAXException {
        File xmlFile = new File(Thread.currentThread().getClass().getResource("/xml/books.xml").getPath());
        Books books = parseXml(xmlFile);
        System.out.println(books);
        System.out.println(JSONObject.toJSONString(books));
    }
}