package sqlexecutor.pool;

import java.util.*;
import java.util.Map.Entry;

/**
 * DBManager数据源缓存池
 *
 * @author cuitianyu
 */
public class DBManagerCache {
    final static int cacheSize = 10;
    private static LinkedHashMap<String, DBManager> cacheMap = new LinkedHashMap<String, DBManager>(
            (int) Math.ceil(cacheSize / 0.75f) + 1, 0.75f, true) {
        private static final long serialVersionUID = 1L;

        protected boolean removeEldestEntry(Map.Entry<String, DBManager> eldest) {
            boolean flag = size() > cacheSize;
            if (flag == true) {
                if (null != eldest.getValue()) {
                    eldest.getValue().closeDataSource();
                }
            }
            return flag;
        }

        public DBManager put(String key, DBManager value) {
            DBManager put = super.put(key, value);
            if (null != put) {
                put.closeDataSource();
            }
            return put;
        }

        public DBManager remove(Object key) {
            DBManager remove = super.remove(key);
            if (null != remove) {
                remove.closeDataSource();
            }
            return remove;
        }

    };

    /**
     * 缓存池构造方法
     */
    private DBManagerCache() {
        super();
    }

    /**
     * 获取数据源
     *
     * @param key
     * @return
     */
    public static DBManager getDb(String key) {
        try {
            return cacheMap.get(key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * 存入数据源，若key已存在 则直接返回false
     *
     * @param key db_id
     * @param db
     * @return
     */
    public synchronized static boolean setDb(String key, DBManager db) {
        if (getDb(key) != null) {// 假如为真不允许被覆盖
            return false;
        } else {
            cacheMap.put(key, db);
            return true;
        }
    }


    /**
     * 存入数据源，若key已存在 则将前一个数据源关闭 重新写入新数据源
     *
     * @param key
     * @param db
     */
    public synchronized static void putDb(String key, DBManager db) {
        cacheMap.put(key, db);
    }

    /**
     * 清除指定的数据源
     *
     * @param key
     */
    public synchronized static void removeDb(String key) {
        cacheMap.remove(key);
    }

    /**
     * 判断已是否存在一个数据源
     *
     * @param key
     * @return
     */
    private synchronized static boolean hasDb(String key) {
        return cacheMap.containsKey(key);
    }

    /**
     * 清除所有数据源
     */
    public synchronized static void removeAll() {
        Object[] keys = cacheMap.keySet().toArray();
        for (Object key : keys) {
            cacheMap.remove(key);
        }
    }

    /**
     * ' 清除某一类特定数据源,通过遍历HASHMAP下的所有对象，来判断它的KEY前缀与传入的TYPE是否匹配
     *
     * @param type 清除以此开头的所有连接
     */
    public synchronized static void removeAll(String type) {
        Iterator<Entry<String, DBManager>> i = cacheMap.entrySet().iterator();
        String key;
        ArrayList<String> arr = new ArrayList<String>();
        try {
            while (i.hasNext()) {
                Entry<String, DBManager> entry = i.next();
                key = entry.getKey();
                if (key.startsWith(type)) { // 如果匹配则删除掉
                    arr.add(key);
                }
            }
            for (int k = 0; k < arr.size(); k++) {
                removeDb(arr.get(k));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * 获取缓存池size
     *
     * @return
     */
    public static int getCacheSize() {
        return cacheMap.size();
    }

    /**
     * 获取指定的类型连接的数量
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
                Entry entry = (java.util.Map.Entry) i.next();
                key = (String) entry.getKey();
                if (key.indexOf(type) != -1) { // 如果匹配则删除掉
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
    public static List<String> getCacheAllkey() {
        List<String> a = new ArrayList<String>();
        try {
            Iterator<Entry<String, DBManager>> i = cacheMap.entrySet().iterator();
            while (i.hasNext()) {
                Entry<String, DBManager> entry = i.next();
                a.add(entry.getKey());
            }
        } catch (Exception ex) {

        }
        return a;
    }

    /**
     * 获取缓存对象中指定类型 的键值名称
     *
     * @param type
     * @return
     */
    public static List<String> getCacheListkey(String type) {
        List<String> a = new ArrayList<String>();
        String key;
        try {
            Iterator<Entry<String, DBManager>> i = cacheMap.entrySet().iterator();
            while (i.hasNext()) {
                Entry<String, DBManager> entry = i.next();
                key = entry.getKey();
                if (key.indexOf(type) != -1) {
                    a.add(key);
                }
            }
        } catch (Exception ex) {
        }
        return a;
    }

}
