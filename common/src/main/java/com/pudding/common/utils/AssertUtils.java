package com.pudding.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.exception.BusinessException;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具类
 */
public class AssertUtils {

    /**
     * 判断是否为False
     * @param valid 判断结果 true抛出异常
     * @param resultCode 返回结果
     */
    public static void isFalse(Boolean valid, ResultCodeEnum resultCode){
        if (valid){
            throw  new BusinessException(resultCode);
        }
    }

    /**
     * 判断是否为True
     * @param valid 判断结果 false抛出异常
     * @param resultCode 返回结果
     */
    public static void isTrue(Boolean valid, ResultCodeEnum resultCode){
        if (!valid){
            throw  new BusinessException(resultCode);
        }
    }
    /**
     * 判断是否为true
     * @param valid 判断结果 false抛出异常
     * @param message 异常信息
     */
    public static void isTrue(Boolean valid,String message){
        if (!valid){
            throw  new BusinessException(message);
        }
    }


    /**
     * 判断字符串是否为空
     * @param str 字符串
     * @param resultCode 响应状态码
     */
    public static void isNotEmpty(String str,ResultCodeEnum resultCode){
        if (StrUtil.isEmpty(str)){
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 如果集合为空或者长度小于1，则抛异常
     *
     * @param collection
     * @param resultCode
     */
    public static void isNotEmpty(Collection collection, ResultCodeEnum resultCode) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 如果字符串为空或者长度小于1，则放行，否则抛异常
     *
     * @param str
     * @param resultCode
     */
    public static void isEmpty(String str, ResultCodeEnum resultCode) {
        if (StrUtil.isNotEmpty(str)) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 如果集合为空或者长度小于1，则放行，否则抛异常
     *
     * @param collection
     * @param resultCode
     */
    public static void isEmpty(Collection collection, ResultCodeEnum resultCode) {
        if (CollectionUtil.isNotEmpty(collection)) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 如果集合为空或者长度小于1，则抛异常
     *
     * @param map
     * @param resultCode
     */
    public static void isNotEmpty(Map map, ResultCodeEnum resultCode) {
        if (MapUtil.isEmpty(map)) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 如果对象是否不为空，不为空则抛异常
     *
     * @param object
     * @param resultCode
     */
    public static void isNull(Object object, ResultCodeEnum resultCode) {
        if (ObjectUtil.isNotNull(object)) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 如果对象是否不为空，不为空则抛异常
     *
     * @param object
     * @param message
     */
    public static void isNull(Object object, String message) {
        if (ObjectUtil.isNotNull(object)) {
            throw new BusinessException(message);
        }
    }

    /**
     * 如果对象是空，则抛异常
     *
     * @param object
     * @param resultCode
     */
    public static void isNotNull(Object object, ResultCodeEnum resultCode) {
        if (ObjectUtil.isNull(object)) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 判断对象数组是否为空
     * @param object 对象数组
     * @param resultCode 返回结果
     */
    public static void isNotNull(Object[] object, ResultCodeEnum resultCode) {
        if (ArrayUtil.isEmpty(object)) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 比较对象
     *
     * @param obj1
     * @param obj2
     * @param resultCode
     */
    public static void equals(Object obj1, Object obj2, ResultCodeEnum resultCode) {
        if (!equals(obj1, obj2)) {
            throw new BusinessException(resultCode);
        }
    }

    /**
     * 比较对象
     *
     * @param obj1
     * @param obj2
     * @param message
     */
    public static void equals(Object obj1, Object obj2, String message) {
        if (!equals(obj1, obj2)) {
            throw new BusinessException(message);
        }
    }

    //比较对象的相等
    public static boolean equals(final Object cs1, final Object cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (null == cs1 && null != cs2) {
            return false;
        }
        if (null != cs1 && null == cs2) {
            return false;
        }
        if (cs1 == null || null == cs2) {
            return true;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        return cs1.equals(cs2);
    }

    
    
}
