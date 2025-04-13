package com.pudding.repository.cache.redis;

import java.util.Set;

/**
 * set类型操作
 */
public interface SetOperations<V> {

    /**
     * 向(key对应的)set中添加items
     *
     * 注: 若key不存在，则会自动创建。
     * 注: set中的元素会去重。
     *
     * @param key
     *            定位set的key
     * @param items
     *            要向(key对应的)set中添加的items
     *
     * @return 此次添加操作,添加到set中的元素的个数
     */
    Long sAdd(String key, V... items);


    /**
     * 从(key对应的)set中删除items
     *
     * 注: 若key不存在, 则返回0。
     * 注: 若已经将(key对应的)set中的项删除完了，那么对应的key也会被删除。
     *
     * @param key
     *            定位set的key
     * @param items
     *            要移除的items
     *
     * @return 实际删除了的个数
     */
    Long sRemove(String key, V... items);


    /**
     * 获取(key对应的)set中的元素个数
     *
     * 注: 若key不存在，则返回0
     *
     * @param key
     *            定位set的key
     *
     * @return  (key对应的)set中的元素个数
     */
    Long sSize(String key);

    /**
     * 判断(key对应的)set中是否含有item
     *
     * 注: 若key不存在，则返回false。
     *
     * @param key
     *            定位set的key
     * @param item
     *            被查找的项
     *
     * @return  (key对应的)set中是否含有item
     */
    Boolean sIsMember(String key, V item);

    /**
     * 获取key对应的set
     *
     * 注: 若key不存在, 则返回的是空的set(, 而不是null)
     *
     * @param key
     *            定位set的key
     * @return  (key对应的)set
     */
    Set<V> sMembers(String key);






}
