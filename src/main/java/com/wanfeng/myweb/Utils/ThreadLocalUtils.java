package com.wanfeng.myweb.Utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;


@UtilityClass
public class ThreadLocalUtils {

    /*使用常量定义一个key*/
    public static final String USER_KEY = "USER_KEY";

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 存储
     *
     * @return void [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public static void put(String key, Object value) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(key, value);
        THREAD_LOCAL.set(map);
    }

    /**
     * 取值
     *
     * @return T [返回类型说明]
     * @see [类、类#方法、类嗯#成员]
     */

    public static <T> T get(String key, Class<T> tClass) {
        Map<String, Object> _map = THREAD_LOCAL.get();
        if (_map != null) {
            return tClass.cast(_map.get(key));
        }
        return tClass.cast(new Object());
    }

    public static void release() {
        THREAD_LOCAL.remove();
    }

}
