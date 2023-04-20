package com.hb.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhaochengshui
 * @description
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
        return (T) map.get(key);
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
     * 重置finally中移除
     */
    public static void reseat() {
        HOLDER_MAP.remove();
    }

    private ThreadLocalContext() {
    }
}
