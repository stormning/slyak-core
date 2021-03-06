package com.slyak.core;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/30
 */
public class ClassUtils {

    private ClassUtils() {
    }

    public static Class getGenericParameter(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (genType instanceof ParameterizedType) {
            return getGenericParameter((ParameterizedType) genType, index);
        }
        return null;
    }

    public static Class getGenericParameter0(Class clazz) {
        return getGenericParameter(clazz, 0);
    }

    public static Class getGenericParameter(Field field, int index) {
        Type genType = field.getGenericType();
        if (genType instanceof ParameterizedType) {
            return getGenericParameter((ParameterizedType) genType, index);
        }
        return null;
    }

    public static Class getGenericParameter0(Field field) {
        return getGenericParameter(field, 0);
    }

    private static Class getGenericParameter(ParameterizedType type, int index) {
        Type param = type.getActualTypeArguments()[index];
        if (param instanceof Class) {
            return (Class) param;
        }
        return null;
    }
}
