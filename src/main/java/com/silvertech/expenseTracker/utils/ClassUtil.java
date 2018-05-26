package com.silvertech.expenseTracker.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {
    public static Field[] findAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> annotatedFields = new ArrayList<Field>(declaredFields.length);
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(annotationClass)) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields.toArray(new Field[annotatedFields.size()]);
    }

    public static Annotation[] findFieldAnnotations(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        return field.getAnnotations();
    }

    public static <T extends Annotation> T findFieldAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        return field.getAnnotation(annotationClass);
    }
}