package com.slyak.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/6/30
 */
public class ClassUtils {

    private ClassUtils(){}

    public static <T> Class<T> getGenericParameter(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (genType instanceof ParameterizedType) {
            return getGenericParameter((ParameterizedType)genType, index);
        }
        return null;
    }

    public static <T> Class<T> getGenericParameter0(Class clazz) {
        return getGenericParameter(clazz, 0);
    }

    public static <T> Class<T> getGenericParameter(Field field, int index) {
        Type genType = field.getGenericType();
        if (genType instanceof ParameterizedType) {
            return getGenericParameter((ParameterizedType)genType, index);
        }
        return null;
    }

    public static <T> Class<T> getGenericParameter0(Field field) {
        return getGenericParameter(field, 0);
    }

    private static <T> Class<T> getGenericParameter(ParameterizedType type, int index) {
        Type param = type.getActualTypeArguments()[index];
        if (param instanceof Class) {
            return (Class) param;
        }
        return null;
    }

    public static Set<Field> getAnnotatedFields(Class clazz,Class<? extends Annotation>... annotations) {
        return doInAnnotatedFields(clazz,new SetFieldExtractor() ,annotations);
    }

    public static Field getAnnotatedField(Class clazz,Class<? extends Annotation>... annotations) {
        return doInAnnotatedFields(clazz,new SingleFieldExtractor() ,annotations);
    }

    public static <T> T doInAnnotatedFields(Class clazz,FieldExtractor<T> extractor, Class<? extends Annotation>... annotations) {
        for (Field field : clazz.getDeclaredFields()) {
            int hit = 0;
            for (Class<? extends Annotation> annotation : annotations) {
                if (field.isAnnotationPresent(annotation)){
                    hit++;
                } else {
                    break;
                }
            }
            if (hit == annotations.length) {
                if (!extractor.extractNext(field)){
                    return extractor.getReturn();
                }
            }
        }
        Class parent = clazz.getSuperclass();
        if (parent != null) {
            return doInAnnotatedFields(parent,extractor,annotations);
        }
        return extractor.getReturn();
    }

    private static class SetFieldExtractor implements FieldExtractor<Set<Field>> {
        Set<Field> result = new HashSet<Field>();

        @Override
        public boolean extractNext(Field field) {
            result.add(field);
            return true;
        }

        @Override
        public Set<Field> getReturn() {
            return result;
        }
    }

    private static class SingleFieldExtractor implements FieldExtractor<Field> {
        Field field = null;
        @Override
        public boolean extractNext(Field field) {
            this.field = field;
            return false;
        }

        @Override
        public Field getReturn() {
            return field;
        }
    }

    public static interface FieldExtractor<T> {
        boolean extractNext(Field field);
        T getReturn();
    }
}
