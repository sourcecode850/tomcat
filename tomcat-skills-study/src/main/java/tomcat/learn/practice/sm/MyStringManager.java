package tomcat.learn.practice.sm;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author niepu
 * @since 2019-11-18
 */
public class MyStringManager {

    /**
     * The ResourceBundle for this StringManager.
     */
    private final ResourceBundle bundle;
    private final Locale locale;

    // 使用私有构造器；
    private MyStringManager(String packageName, Locale locale) {
        String bundleName = packageName + ".LocalStrings";
        ResourceBundle bnd = null;
        try {
            // 没有获取到bnd，为null
            bnd = ResourceBundle.getBundle(bundleName, locale);
        } catch (MissingResourceException ex) {
            // Try from the current loader (that's the case for trusted apps)
            // Should only be required if using a TC5 style classloader structure
            // where common != shared != server
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                try {
                    bnd = ResourceBundle.getBundle(bundleName, locale, cl);
                } catch (MissingResourceException ex2) {
                    // Ignore
                }
            }
        }
        bundle = bnd;
        // Get the actual locale, which may be different from the requested one
        if (bundle != null) {
            Locale bundleLocale = bundle.getLocale();
            if (bundleLocale.equals(Locale.ROOT)) {
                this.locale = Locale.ENGLISH;
            } else {
                this.locale = bundleLocale;
            }
        } else {
            this.locale = null;
        }
    }

    public String getString(String key) {
        if (key == null) {
            String msg = "key may not have a null value";
            throw new IllegalArgumentException(msg);
        }

        String str = null;

        try {
            // Avoid NPE if bundle is null and treat it like an MRE
            if (bundle != null) {
                str = bundle.getString(key);
            }
        } catch (MissingResourceException mre) {
            str = null;
        }

        return str;
    }


    public static final MyStringManager getManager(Class<?> clazz, Locale locale) {
        return new MyStringManager(clazz.getPackage().getName(), locale);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

        MyStringManager myStringManager = MyStringManager.getManager(MyStringManager.class, Locale.getDefault());

        System.out.println(myStringManager.getString("resource.bundle.prefix"));
        System.out.println(new String(myStringManager.getString("resource.bundle.prefix").getBytes("ISO-8859-1"), "UTF-8"));

        // 占位符测试
        String s = myStringManager.getString("resource.bundle.prefix.placeHolder");
        System.out.println(new String(s.getBytes("ISO-8859-1"), "UTF-8"));
        Object[] array = new Object[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q"};
        String value = MessageFormat.format(new String(s.getBytes("ISO-8859-1"), "UTF-8"), array);
        System.out.println(value);

    }
}
