package tomcat.learn.practice.sm;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author niepu
 * @since 2019-11-18
 */
public class MyCache {

    // 构造器最后的accessOrder参数的含义是：如果为true的话
    // 如果accessOrder为true的话，则会把访问过的元素放在链表后面，放置顺序是访问的顺序
    // 如果accessOrder为flase的话，则按插入顺序来遍历；默认accessOrder是false
    Map<String, String> cache;

    private int cacheSize;

    public MyCache() {
        this(15);
    }

    public MyCache(int cacheSize) {
        this.cacheSize = cacheSize;
        // 虽然这初始化map的capacity的时候，会使用2的幂数，我们少写点，底层会帮我们处理成不小于CacheSize的最小的2的幂数
        cache = new LinkedHashMap<String, String>(cacheSize, 1, true) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(
                    Map.Entry<String, String> eldest) {
                if (size() > cacheSize - 1) {
                    return true;
                }
                return false;
            }
        };
    }


    @Test
    public void testAccessOrderTrue() {

        Map<String, String> cache = new LinkedHashMap<String, String>(16, 1, true);
        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");

        // {1=1, 2=2, 3=3, 4=4}
        System.out.println(cache);

        cache.get("3");
        cache.get("2");
        // {1=1, 4=4, 3=3, 2=2}
        System.out.println(cache);

    }

    @Test
    public void testAccessOrderFalse() {

        Map<String, String> cache = new LinkedHashMap<String, String>(16, 1, false);
        cache.put("1", "1");
        cache.put("2", "2");
        cache.put("3", "3");
        cache.put("4", "4");

        // {1=1, 2=2, 3=3, 4=4}
        System.out.println(cache);

        cache.get("3");
        cache.get("2");
        // {1=1, 2=2, 3=3, 4=4}
        System.out.println(cache);

    }


    public synchronized String get(String s) {
        return cache.get(s);
    }

    public synchronized void put(String key, String value) {
        cache.put(key, value);
    }


    @Test
    public void localeDefault(){
        System.out.println(Locale.getDefault());
    }

}
