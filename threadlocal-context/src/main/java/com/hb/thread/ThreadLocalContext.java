package com.hb.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaochengshui
 * @description threadLocal context , Used for thread-level data storage , reseat after use
 * @date 2023/4/20
 */
public class ThreadLocalContext {

    private static final ThreadLocal<HashMap<String, Object>> HOLDER_MAP = new ThreadLocal<>();

    /**
     * 查询 key 对应的 threadLocal数据数组
     *
     * @param key key
     * @return thread local list data
     */
    public static <T> List<T> getHolderListByKey(String key) {
        HashMap<String, Object> map = HOLDER_MAP.get();
        if (map == null || map.isEmpty()) {
            return new ArrayList<>();
        }
        return (List<T>) map.get(key);
    }

    /**
     * 查询 key 对应的 threadLocal数据
     *
     * @param key key
     * @return thread local data
     */
    public static <T> T getLocalByKey(String key) {
        HashMap<String, Object> map = HOLDER_MAP.get();
        if (map == null || map.isEmpty()) {
            return null;
        }
        Object value = map.get(key);
        return (T) value;
    }

    /**
     * threadLocal数据设置值
     *
     * @param key  key
     * @param date thread local data
     */
    public static void setHolderByKey(String key, Object date) {
        HashMap<String, Object> map = HOLDER_MAP.get();
        if (map == null || map.isEmpty()) {
            map = new HashMap<>();
        }
        map.put(key, date);
        HOLDER_MAP.set(map);
    }

    /**
     * 返回 thread local map
     *
     * @return thread local map
     */
    public static Map<String, Object> getHolderMap() {
        return HOLDER_MAP.get();
    }

    /**
     * 返回指定keys的map
     *
     * @param keys keys
     * @return map
     */
    public static Map<String, Object> getHolderMapByKeys(String... keys) {
        HashMap<String, Object> map = HOLDER_MAP.get();
        HashMap<String, Object> newMap = new HashMap<>();
        if (map == null || map.isEmpty()) {
            return newMap;
        }
        for (String key : keys) {
            if (map.containsKey(key)) {
                newMap.put(key, map.get(key));
            }
        }
        return newMap;
    }

    /**
     * 重置finally中移除
     */
    public static void reseat() {
        HOLDER_MAP.remove();
    }

    private ThreadLocalContext() {
    }
}
