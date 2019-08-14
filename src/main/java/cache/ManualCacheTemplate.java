package cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class ManualCacheTemplate {


    private static HashMap<String, List<String>> cacheMap = new HashMap<String, List<String>>();

    /**
     * 单实例构造方法
     */
    private ManualCacheTemplate() {
        super();
    }

    //获取布尔值的缓存
    public static List<String> getSimpleDb(String key) {
        try {
            return cacheMap.get(key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * 设置布尔值的缓存
     *
     * @param key
     * @param db
     * @return
     */
    public synchronized static boolean setSimpledb(String key, List<String> tags) {
        if (getSimpleDb(key) != null) {//假如为真不允许被覆盖
            return false;
        } else {
            cacheMap.put(key, tags);
            return true;
        }
    }


    /**
     * 得到缓存。同步静态方法
     *
     * @param key
     * @return
     */
    private synchronized static List<String> getDb(String key) {
        return cacheMap.get(key);
    }

    /**
     * 判断是否存在一个缓存
     *
     * @param key
     * @return
     */
    private synchronized static boolean hasDb(String key) {
        return cacheMap.containsKey(key);
    }

    /**
     * 清除所有缓存
     */
    public synchronized static void clearAll() {
        cacheMap.clear();
    }

    /**
     * '
     * 清除某一类特定缓存,通过遍历HASHMAP下的所有对象，来判断它的KEY与传入的TYPE是否匹配
     *
     * @param type
     */
    public synchronized static void clearAll(String type) {
        Iterator i = cacheMap.entrySet().iterator();
        String key;
        ArrayList<String> arr = new ArrayList<String>();
        try {
            while (i.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
                key = (String) entry.getKey();
                if (key.startsWith(type)) { //如果匹配则删除掉
                    arr.add(key);
                }
            }
            for (int k = 0; k < arr.size(); k++) {
                clearOnly(arr.get(k));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 清除指定的缓存
     *
     * @param key
     */
    public synchronized static void clearOnly(String key) {
        cacheMap.remove(key);
    }

    /**
     * 载入缓存
     *
     * @param key
     * @param db
     */
    public synchronized static void putDb(String key, List<String> tags) {
        cacheMap.put(key, tags);
        //System.out.println(cacheMap);
    }


    /**
     * 获取缓存中的大小
     *
     * @return
     */
    public static int getCacheSize() {
        return cacheMap.size();
    }

    /**
     * 获取指定的类型的大小
     *
     * @param type
     * @return
     */
    public static int getCacheSize(String type) {
        int k = 0;
        Iterator i = cacheMap.entrySet().iterator();
        String key;
        try {
            while (i.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
                key = (String) entry.getKey();
                if (key.indexOf(type) != -1) { //如果匹配则删除掉
                    k++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return k;
    }

    /**
     * 获取缓存对象中的所有键值名称
     *
     * @return
     */
    public static ArrayList getCacheAllkey() {
        ArrayList a = new ArrayList();
        try {
            Iterator i = cacheMap.entrySet().iterator();
            while (i.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
                a.add(entry.getKey());
            }
        } catch (Exception ex) {
        } finally {
            return a;
        }
    }

    /**
     * 获取缓存对象中指定类型 的键值名称
     *
     * @param type
     * @return
     */
    public static ArrayList getCacheListkey(String type) {
        ArrayList a = new ArrayList();
        String key;
        try {
            Iterator i = cacheMap.entrySet().iterator();
            while (i.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) i.next();
                key = (String) entry.getKey();
                if (key.indexOf(type) != -1) {
                    a.add(key);
                }
            }
        } catch (Exception ex) {
        } finally {
            return a;
        }
    }


}
