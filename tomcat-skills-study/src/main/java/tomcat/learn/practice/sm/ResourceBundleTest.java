package tomcat.learn.practice.sm;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author niepu
 * @since 2019-11-19
 */
public class ResourceBundleTest {
    public static void main(String[] args) throws UnsupportedEncodingException {

        // 将资源设置为中国
        Locale locale1 = new Locale("zh", "CN");
        if (locale1.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            locale1 = Locale.ROOT;
        }
        ResourceBundle res1 = ResourceBundle.getBundle("sm/LocalStrings", locale1);//res为工程根目录下的res.properties文件
        System.out.println(res1.getString("resource.bundle.prefix"));

        // 将资源设置为默认国别
        ResourceBundle res2 = ResourceBundle.getBundle("sm/LocalStrings", Locale.getDefault());
        // 乱码处理
        System.out.println(new String(res2.getString("resource.bundle.prefix").getBytes("ISO-8859-1"), "utf-8"));
        // 将资源设置为美国
        Locale locale3 = new Locale("en", "US");
        // 如果是en设置成ROOT，即lang是空，就会使用默认的资源文件
        if (locale3.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            locale3 = Locale.ROOT;
        }
        ResourceBundle res3 = ResourceBundle.getBundle("sm/LocalStrings", locale3);
        System.out.println(res3.getString("resource.bundle.prefix"));

    }
}
