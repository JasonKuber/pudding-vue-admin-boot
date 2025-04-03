package com.pudding.repository.cache.redis;

import java.util.concurrent.TimeUnit;

/**
 * String 类型操作
 *
 * @param <V>
 */
public interface StringOperations<V> {

    /**
     * 设置缓存值
     *
     * @param key   键名
     * @param value 值
     */
    void set(String key, V value);

    /**
     * 设置缓存并指定过期时间
     *
     * @param key      键值
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 过期单位
     */
    void set(String key, V value, long timeout, TimeUnit timeUnit);

    /**
     * 获取缓存值
     *
     * @param key 键值
     * @return 值
     */
    V get(String key);

    /**
     * 若不存在key时, 向redis中添加key-value, 返回成功/失败。
     * 若存在，则不作任何操作, 返回false。
     *
     * @param key   键值
     * @param value 值
     * @return 是否添加成功
     */
    boolean setIfAbsent(String key, V value);

    /**
     * 若不存在key时, 向redis中添加具有超时时长的key-value, 返回成功/失败。
     * 若存在，则不作任何操作, 返回false。
     *
     * @param key     键值
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 是否添加成功
     */
    boolean setIfAbsent(String key, V value, long timeout, TimeUnit unit);

    /**
     * 增/减 整数
     * 负数则为减。
     * 若key对应的value值不支持增/减操作(即: value不是数字)， 那么会
     * 抛出org.springframework.data.redis.RedisSystemException
     *
     * @param key 键值
     * @param increment 操作值(增加或减少多少)
     * @return 操作后的总值
     */
    long incrBy(String key, long increment);

    /**
     * 增/减 浮点
     * 负数则为减。
     * 若key对应的value值不支持增/减操作(即: value不是数字)， 那么会
     * 抛出org.springframework.data.redis.RedisSystemException
     *
     * @param key 键值
     * @param increment 操作值(增加或减少多少)
     * @return 操作后的总值
     */
    double incrByFloat(String key, double increment);


}
