package com.vector.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @Title: BeanCopyUtils
 * @Package com.vector.utils
 * @Author 芝士汉堡
 * @Date 2022/9/18 12:52
 */

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象（V 创建的对象即使传过来的对象）
        V result = null;
        try {
            result = clazz.newInstance();//利用反射
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
