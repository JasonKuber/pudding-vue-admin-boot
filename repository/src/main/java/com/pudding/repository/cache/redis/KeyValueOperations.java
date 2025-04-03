package com.pudding.repository.cache.redis;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * key相关操作
 */
public interface KeyValueOperations {



    /**
     * 删除key
     * @param key 键值
     * @return 是否删除成功
     */
    Boolean delete(String key);

    /**
     * key是否存在
     * @param key 键值
     * @return 是否存在
     */
    Boolean hasKey(String key);

    /**
     * 设置key的过期时间
     * @param key 键值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    void expire(String key, long timeout, TimeUnit unit);

    /**
     * 设置key的过期时间
     * @param key 键值
     * @param date 过期时间
     */
    void expireAt(String key, Date date);

    /**
     * 获取key的过期时间
     * @param key 键值
     * @param unit 时间单位
     * @return
     */
    long getExpire(String key,TimeUnit unit);
}
