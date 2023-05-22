package com.zihe.tams.common.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.*;
import org.springframework.util.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 对象转换工具类
 */
@Slf4j
public class ConvertUtils {

    private static final String REFLECT_ERROR = "反射获取转换对象异常";

    /**
     * 英文逗号：,
     */
    private static final String COMMA = ",";

    private ConvertUtils() {
    }



    /**
     * map转对象
     *
     * @param map    散列表
     * @param object 对象
     */
    public static void mapToObject(Map map, Object object) {
        if (object != null && map != null) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(object);
            PropertyValues propertyValues = new MutablePropertyValues(map);
            beanWrapper.setPropertyValues(propertyValues, true, true);
        }
    }

    /**
     * 下划线风格的字段转成驼峰
     */
    public static String changeColumnToFieldName(String columnName) {
        String[] array = columnName.split("_");
        StringBuilder sb = null;
        for (String cn : array) {
            cn = cn.toLowerCase();
            if (sb == null) {
                sb = new StringBuilder(cn);
                continue;
            }
            sb.append(cn.substring(0, 1).toUpperCase()).append(cn.substring(1));
        }
        return sb != null ? sb.toString() : null;
    }

    /**
     * 反射实现对象转Map <br>
     * 只针对实体里所有属性, 包括继承的属性 <br>
     * 空属性不会转换 <br>
     *
     * @param obj 需要转换的对象
     * @return 转换后的map
     * @author fuqiang
     */
    public static Map<String, Object> objectToMap(Object obj) {
        Collection<String> ignoredFieldCol = new HashSet<>();
        return objectToMap(obj, ignoredFieldCol);
    }

    public static Map<String, Object> objectToMap(Object obj, Collection<String> ignoredFieldCol) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        if (obj instanceof String) {
            map.put((String) obj, obj);
            return map;
        }

        copyProperty(obj, map, true, ignoredFieldCol);
        return map;
    }

    /**
     * 对象转map<br/>
     *
     * @param object 对象
     * @param map    散列表
     */
    public static void objectToMap(Object object, Map map) {
        if (object != null && map != null) {
            copyProperty(object, map, true);
        }
    }

    /**
     * 属性copy
     *
     * @param source  源对象
     * @param target  目标map
     * @param cascade 是否级联copy（true：copy含父类属性；false：copy不含父类属性）
     */
    public static void copyProperty(Object source, Map target, boolean cascade) {
        if (Objects.nonNull(source) && Objects.nonNull(target)) {
            copyProperty(source, target, cascade, null);
        }
    }

    /**
     * 属性copy
     *
     * @param source          源对象
     * @param target          目标map
     * @param cascade         是否级联copy（true：copy含父类属性；false：copy不含父类属性）
     * @param ignoredFieldCol 忽略的字段集合
     */
    public static void copyProperty(Object source, Map target, boolean cascade,
                                    Collection<String> ignoredFieldCol) {
        Field[] fields = source.getClass().getDeclaredFields();
        if (cascade) {
            Field[] parentFields = source.getClass().getSuperclass().getDeclaredFields();
            fields = ArrayUtils.addAll(fields, parentFields);
        }
        if (Objects.isNull(ignoredFieldCol)) {
            ignoredFieldCol = new HashSet<>();
        }
        ignoredFieldCol.add("serialVersionUid");
        try {
            for (Field field : fields) {
                if (ignoredFieldCol.contains(field.getName())) {
                    continue;
                }
                field.setAccessible(true);
                if (field.get(source) != null) {
                    target.put(field.getName(), field.get(source));
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error(REFLECT_ERROR, e);
        }
    }

    /**
     * List<Map<String,Object>>转List<T>
     *
     * @param mapList    数据源列表
     * @param objectList 数据接收列表
     * @param clazz      接收实体类型
     */
    public static void listMapToListObject(List<Map<String, Object>> mapList, List objectList, Class clazz) {
        if (CollectionUtils.isNotEmpty(mapList) && objectList != null) {
            for (Map map : mapList) {
                Object object = gainInstanceByReflect(clazz);
                mapToObject(map, object);
                objectList.add(object);
            }
        }
    }

    /**
     * 对象列表转对象列表
     *
     * @param sourceList  源列表
     * @param targetList  目标列表
     * @param targetClass 目标类型
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void listObjectToListObject(List sourceList, List targetList, Class targetClass) {
        if (CollectionUtils.isNotEmpty(sourceList) && targetList != null) {
            for (Object source : sourceList) {
                Object target = gainInstanceByReflect(targetClass);
                BeanUtils.copyProperties(source, target);
                targetList.add(target);
            }
        }
    }

    /**
     * 列表对象转换字符串 : 以英文逗号(,)分隔, 不需要前后缀
     *
     * @param list 列表
     * @return 字符串
     */
    public static String listToString(List<String> list) {
        return listToString(list, COMMA);
    }

    /**
     * 列表对象转换字符串 : 不需要前后缀
     *
     * @param list      列表
     * @param separator 字符串分隔符
     * @return 字符串
     */
    public static String listToString(List<String> list, String separator) {
        return listToString(list, separator, null);
    }

    /**
     * 列表对象转换字符串
     *
     * @param list      列表
     * @param separator 字符串分隔符
     * @param surround  字符串的前后缀，null表示不需要包裹
     * @return 字符串
     */
    public static String listToString(List<String> list, String separator, String surround) {
        StringBuilder builder = new StringBuilder();

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (String str : list) {
                if (i++ > 0) {
                    builder.append(separator);
                }

                if (surround != null) {
                    builder.append(surround).append(str).append(surround);
                } else {
                    builder.append(str);
                }
            }
        }
        return builder.toString();
    }

    /**
     * 反射获取对象实例
     *
     * @param clazz 目标类
     * @return 对象实例
     */
    public static Object gainInstanceByReflect(Class clazz) {
        String className = clazz.getName();
        return gainInstanceByReflect(className);
    }

    /**
     * 反射获取对象实例
     *
     * @param className 类名
     * @return 对象实例
     */
    private static Object gainInstanceByReflect(String className) {
        Object target = null;
        try {
            target = Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            log.error(REFLECT_ERROR, e);
        }
        return target;
    }

    /**
     * 对象属性copy
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void objectToObject(Object source, Object target) {
        objectToObject(source, target, false, true, true);
    }


    /**
     * 对象属性copy
     *
     * @param source     源对象
     * @param target     目标对象
     * @param ignoreNull 忽略NULL属性
     */
    public static void objectToObject(Object source, Object target, boolean ignoreNull) {
        objectToObject(source, target, ignoreNull, false, false);
    }

    /**
     * 对象属性copy
     *
     * @param source         源对象
     * @param target         目标对象
     * @param ignoreNull     忽略NULL属性
     * @param withCreateInfo copy包括创建操作信息
     * @param withUpdateInfo copy包括更新操作信息
     */
    public static void objectToObject(Object source, Object target, boolean ignoreNull,
                                      boolean withCreateInfo, boolean withUpdateInfo) {
        if (source == null || target == null) {
            return;
        }

        List<String> ignorePropertiesList = new ArrayList<>();
        // 处理旧债务
        // 说明：
        // 1、对于更新/新增操作，所有的VoRequest都应该继承BaseRequest，让以下四个参数在线程中传递
        // 2、对于查询操作，应该另起一个FormVo，比如xxxRequestQueryVo，不应该继承BaseRequest，否则会造成以下四个参数被作为查询参数
        if (!withCreateInfo) {
            ignorePropertiesList.add("creator");
            ignorePropertiesList.add("creatorCode");
            ignorePropertiesList.add("createTime");

        }
        if (!withUpdateInfo) {
            ignorePropertiesList.add("updater");
            ignorePropertiesList.add("updaterCode");
            ignorePropertiesList.add("updateTime");

        }
        if (ignoreNull) {
            ignoreNull(source, ignorePropertiesList);
        }
        String[] ignorePropertiesArray = new String[ignorePropertiesList.size()];
        BeanUtils.copyProperties(source, target, ignorePropertiesList.toArray(ignorePropertiesArray));
    }

    /**
     * 忽略null属性
     *
     * @param source               源
     * @param ignorePropertiesList 忽略的属性队列
     */
    private static void ignoreNull(Object source, List<String> ignorePropertiesList) {
        Field[] fields = source.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(source) == null) {
                    ignorePropertiesList.add(field.getName());
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error(REFLECT_ERROR, e);
        }
    }


    /**
     * 支持更新操作的对象拷贝
     *
     * @param source     源对象
     * @param target     目标对象
     * @param ignoreNull 忽略NULL属性
     * @param isUpdate   是否更新操作
     */
    public static void objectToObject(Object source, Object target, boolean ignoreNull, boolean isUpdate) {
        objectToObject(source, target, ignoreNull, !isUpdate, true);
    }

    /**
     * Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
     *
     * @param map Map集合
     * @param obj 结果对象
     */
    public static void transMap2Bean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }

            }

        } catch (Exception e) {
            log.error("Map --> Bean Error {} {}", map, obj, e);
        }
    }

    /**
     * 将参数对象转化成目标对象类型
     *
     * @param q           待转化的对象
     * @param targetClass 目录对象类型Class
     * @param <T>         目标对象类型
     * @param <Q>         待转化的对象类型
     * @return 目标对象类型， 异常时返回null
     */
    public static <T, Q> T of(Q q, Class<T> targetClass) {
        if (Objects.isNull(q)) {
            return null;
        }
        T t = BeanUtils.instantiate(targetClass);
        objectToObject(q, t, true, true, true);
        return t;
    }

    /**
     * 将参数对象转化成目标对象类型
     *
     * @param map         待转化的对象
     * @param targetClass 目录对象类型Class
     * @param <T>         目标对象类型
     * @return 目标对象类型， 异常时返回null
     */
    public static <T> T ofMap(Map<String, Object> map, Class<T> targetClass) {
        if (Objects.isNull(map) || map.isEmpty()) {
            return null;
        }
        T t = BeanUtils.instantiate(targetClass);
        mapToObject(map, t);
        return t;
    }

    /**
     * 将参数对象转化成目标对象类型(列表)
     *
     * @param sources     待转化的对象列表
     * @param targetClass 目录对象类型Class
     * @param <T>         目标对象类型
     * @param <Q>         待转化的对象类型
     * @return 目标对象类型， 异常时返回null
     */
    public static <T, Q> List<T> ofList(List<Q> sources, Class<T> targetClass) {

        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }

        List<T> targetList = new ArrayList<>();
        listObjectToListObject(sources, targetList, targetClass);
        return targetList;
    }

    /**
     * 将参数对象转化成目标对象类型(列表)
     *
     * @param sources     待转化的对象列表
     * @param targetClass 目录对象类型Class
     * @param <T>         目标对象类型
     * @return 目标对象类型， 异常时返回null
     */
    public static <T> List<T> ofMapList(List<Map<String, Object>> sources, Class<T> targetClass) {

        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }

        List<T> targetList = new ArrayList<>();
        listMapToListObject(sources, targetList, targetClass);
        return targetList;
    }

    /* ********************** 下面是 common包ConvertUtils 内容 *********************** */

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }


    /**
     * 忽略源对象中的null值
     */
    public static void sourceToTargetWithoutNull(Object source, Object target) {
        if (ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(target)) {
            return;
        }
        final String[] nullPropertyNames = getNullPropertyNames(source);
        BeanUtils.copyProperties(source, target, nullPropertyNames);
    }

    /**
     * 单个对象转换
     *
     * @param source 来源对象
     * @param target 目标对象类
     * @param <T>    目标对象类型
     * @return 目标对象
     */
    public static <T> T sourceToTarget(Object source, Class<T> target) {
        return sourceToTarget(source, target, (Class<?>) null);
    }


    /**
     * 单个对象转换,只把源对象的 targetSuper的属性同步到目标对象
     *
     * @param source      来源对象
     * @param target      目标对象类
     * @param <T>         目标对象类型
     * @param targetSuper 目标对象的父类
     * @return 目标对象
     */
    public static <T> T sourceToTarget(Object source, Class<T> target, Class<?> targetSuper) {
        if (source == null) {
            return null;
        }
        T targetObject = null;
        try {
            targetObject = target.newInstance();
            BeanUtils.copyProperties(source, targetObject, targetSuper);
        } catch (Exception e) {
            log.error("convert error {} {} {}", source, target, targetSuper, e);
        }

        return targetObject;
    }

    /**
     * 数组转换
     *
     * @param sourceList 来源对象数组
     * @param target     目标对象类
     * @param <T>        目标对象类型
     * @return 目标对象数组
     */
    public static <T> List<T> sourceToTarget(Collection<?> sourceList, Class<T> target) {
        if (sourceList == null) {
            return null;
        }
        List targetList = new ArrayList<>(sourceList.size());
        try {
            for (Object source : sourceList) {
                T targetObject = target.newInstance();
                BeanUtils.copyProperties(source, targetObject);
                targetList.add(targetObject);
            }
        } catch (Exception e) {
            log.error("convert error {} {} {}", sourceList, target, e);
        }
        return targetList;
    }


    /**
     * 单个转换，提供忽视字段
     *
     * @param source           来源对象
     * @param target           目标对象类
     * @param ignoreProperties 忽视字段
     * @param <T>              目标对象类型
     * @return 目标对象
     */
    public static <T> T sourceToTarget(Object source, Class<T> target, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        T targetObject = null;
        try {
            targetObject = target.newInstance();
            BeanUtils.copyProperties(source, targetObject, ignoreProperties);
        } catch (Exception e) {
            log.error("convert error {} {}", source, target, e);
        }

        return targetObject;
    }

    /**
     * 数组转换，提供忽视字段
     *
     * @param sourceList       来源对象数组
     * @param target           目标对象类
     * @param ignoreProperties 忽视字段
     * @param <T>              目标对象类型
     * @return 目标对象数组
     */
    public static <T> List<T> sourceToTarget(Collection<?> sourceList, Class<T> target,
                                             String... ignoreProperties) {
        if (sourceList == null) {
            return null;
        }

        List targetList = new ArrayList<>(sourceList.size());
        try {
            for (Object source : sourceList) {
                T targetObject = target.newInstance();
                BeanUtils.copyProperties(source, targetObject, ignoreProperties);
                targetList.add(targetObject);
            }
        } catch (Exception e) {
            log.error("convert error {} {}", sourceList, target, e);
        }

        return targetList;
    }

}