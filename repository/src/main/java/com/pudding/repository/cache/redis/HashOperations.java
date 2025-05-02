package com.pudding.repository.cache.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Hash操作
 * @param <HK>
 * @param <HV>
 */
public interface HashOperations<HK,HV> {

    /**
     * 向key对应的hash中，增加一个键值对entryKey-entryValue
     *
     * 注: 同一个hash里面，若已存在相同的entryKey， 那么此操作将丢弃原来的entryKey-entryValue，
     *     而使用新的entryKey-entryValue。
     *
     *
     * @param key
     *            定位hash的key
     * @param entryKey
     *            要向hash中增加的键值对里的 键
     * @param entryValue
     *            要向hash中增加的键值对里的 值
     */
    void hPut(String key, HK entryKey, HV entryValue);

    /**
     * 向key对应的hash中，增加maps(即: 批量增加entry集)
     *
     * 注: 同一个hash里面，若已存在相同的entryKey， 那么此操作将丢弃原来的entryKey-entryValue，
     *     而使用新的entryKey-entryValue
     *
     * @param key
     *            定位hash的key
     * @param maps
     *            要向hash中增加的键值对集
     */
    void hPutAll(String key, Map<HK,HV> maps);

    /**
     * 获取到key对应的hash里面的对应字段的值
     *
     * 注: 若redis中不存在对应的key, 则返回null。
     *     若key对应的hash中不存在对应的entryKey, 也会返回null。
     *
     * @param key
     *            定位hash的key
     * @param entryKey
     *            定位hash里面的entryValue的entryKey
     *
     * @return  key对应的hash里的entryKey对应的entryValue值
     */
    HV hGet(String key, HK entryKey);

    /**
     * 获取到key对应的hash(即: 获取到key对应的Map<HK, HV>)
     *
     * 注: 若redis中不存在对应的key, 则返回一个没有任何entry的空的Map(，而不是返回null)。
     *
     * @param key
     *            定位hash的key
     *
     * @return  key对应的hash。
     */
    Map<HK, HV> hGetAll(String key);

    /**
     * 批量获取(key对应的)hash中的entryKey的entryValue
     *
     * 注: 若hash中对应的entryKey不存在，那么返回的对应的entryValue值为null
     * 注: redis中key不存在，那么返回的List中，每个元素都为null。
     *     追注: 这个List本身不为null, size也不为0， 只是每个list中的每个元素为null而已。
     *
     * @param key
     *            定位hash的key
     * @param entryKeys
     *            需要获取的hash中的字段集
     * @return  hash中对应entryKeys的对应entryValue集
     */
    List<HV> hMultiGet(String key, Collection<HK> entryKeys);

    /**
     * (批量)删除(key对应的)hash中的对应entryKey-entryValue
     *
     * 注: 1、若redis中不存在对应的key, 则返回0;
     *     2、若要删除的entryKey，在key对应的hash中不存在，在count不会+1, 如:
     *                 RedisUtil.HashOps.hPut("ds", "name", "邓沙利文");
     *                 RedisUtil.HashOps.hPut("ds", "birthday", "1994-02-05");
     *                 RedisUtil.HashOps.hPut("ds", "hobby", "女");
     *                 则调用RedisUtil.HashOps.hDelete("ds", "name", "birthday", "hobby", "non-exist-entryKey")
     *                 的返回结果为3
     * 注: 若(key对应的)hash中的所有entry都被删除了，那么该key也会被删除
     *
     * @param key
     *            定位hash的key
     * @param entryKeys
     *            定位要删除的entryKey-entryValue的entryKey
     *
     * @return 删除了对应hash中多少个entry
     */
    Long hDelete(String key, HK... entryKeys);

    /**
     * 查看(key对应的)hash中，是否存在entryKey对应的entry
     *
     * 注: 若redis中不存在key,则返回false。
     * 注: 若key对应的hash中不存在对应的entryKey, 也会返回false。
     *
     * @param key
     *            定位hash的key
     * @param entryKey
     *            定位hash中entry的entryKey
     *
     * @return  hash中是否存在entryKey对应的entry.
     */
    Boolean hExists(String key, HK entryKey);

    /**
     * 获取(key对应的)hash中的所有entryKey
     *
     * 注: 若key不存在，则返回的是一个空的Set(，而不是返回null)
     *
     * @param key
     *            定位hash的key
     *
     * @return  hash中的所有entryKey
     */
    Set<HK> hKeys(String key);

    /**
     * 获取(key对应的)hash中的所有entryValue
     *
     * 注: 若key不存在，则返回的是一个空的List(，而不是返回null)
     *
     * @param key
     *            定位hash的key
     *
     * @return  hash中的所有entryValue
     */
    List<HV> hValues(String key);

    /**
     * 当key对应的hash中,不存在entryKey时，才(向key对应的hash中，)增加entryKey-entryValue
     * 否者，不进行任何操作
     *
     * @param key
     *            定位hash的key
     * @param entryKey
     *            要向hash中增加的键值对里的 键
     * @param entryValue
     *            要向hash中增加的键值对里的 值
     *
     * @return 操作是否成功。
     */
    boolean hPutIfAbsent(String key, HK entryKey, HV entryValue);


}
