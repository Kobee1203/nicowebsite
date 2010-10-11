package org.nds.dbdroid.reflect.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AnnotationUtils {

    public static List<Annotation> getAnnotations(Class<?> clazz, boolean recursive, boolean includeNonPublic) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        List<Annotation> annotations = new ArrayList<Annotation>();
        // Add all class annotations
        annotations.addAll(getClassAnnotations(clazz));
        // Add all fields annotations
        annotations.addAll(getFieldsAnnotations(clazz, recursive, includeNonPublic));
        // Add all methods annotations
        annotations.addAll(getMethodsAnnotations(clazz, recursive, includeNonPublic));

        return annotations;
    }

    public static Set<Annotation> getClassAnnotations(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        Set<Annotation> annotations = new LinkedHashSet<Annotation>();
        for (Annotation annotation : clazz.getAnnotations()) {
            annotations.add(annotation);
        }
        return annotations;
    }

    @SuppressWarnings("unchecked")
    public <T extends Annotation> T getClassAnnotation(Class<?> clazz, Class<T> annotationType) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }
        if (annotationType == null) {
            throw new IllegalArgumentException("annotationType must not be null");
        }

        T annote = null;
        for (Annotation annotation : getClassAnnotations(clazz)) {
            if (annotationType.equals(annotation.annotationType())) {
                annote = (T) annotation;
                break;
            }
        }

        return annote;
    }

    // FIELDS

    public static Set<Annotation> getFieldsAnnotations(Class<?> clazz, boolean recursive, boolean includeNonPublic) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        Set<Annotation> annotations = new LinkedHashSet<Annotation>();
        for (Field field : ReflectUtils.getFields(clazz, recursive, includeNonPublic)) {
            annotations.addAll(getFieldAnnotations(clazz, field.getName(), includeNonPublic));
        }

        return annotations;
    }

    public static Set<Annotation> getFieldAnnotations(Class<?> clazz, String fieldName, boolean includeNonPublic) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }
        if (fieldName == null) {
            throw new IllegalArgumentException("fieldName must not be null");
        }

        try {
            clazz.getField(fieldName);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static <T extends Annotation> T getFieldAnnotation(Class<?> clazz, Class<T> annotationType, String fieldName) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }
        if (annotationType == null) {
            throw new IllegalArgumentException("annotationType must not be null");
        }
        if (fieldName == null) {
            throw new IllegalArgumentException("fieldName must not be null");
        }
    }

    public static String getFieldNameByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationType) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }
        if (annotationType == null) {
            throw new IllegalArgumentException("annotationType must not be null");
        }
    }

    public static List<String> getFieldNamesWithAnnotation(Class<? extends Annotation> annotationType) {
        if (annotationType == null) {
            throw new IllegalArgumentException("annotationType must not be null");
        }
    }

    // METHODS

    public static Set<Annotation> getMethodsAnnotations(Class<?> clazz, boolean recursive, boolean includeNonPublic) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        List<Method> methods = ReflectUtils.getMethods(clazz, recursive, includeNonPublic);
    }

    public static Set<Annotation> getMethodAnnotations(Class<?> clazz, String methodName) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }
        if (methodName == null) {
            throw new IllegalArgumentException("methodName must not be null");
        }
    }

    public static <T extends Annotation> T getMethodAnnotation(Class<?> clazz, Class<T> annotationType, String methodName) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }
        if (annotationType == null) {
            throw new IllegalArgumentException("annotationType must not be null");
        }
        if (methodName == null) {
            throw new IllegalArgumentException("methodName must not be null");
        }
    }

    public static String getMethodNameByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationType) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }
        if (annotationType == null) {
            throw new IllegalArgumentException("annotationType must not be null");
        }
    }

    public static List<String> getMethodNamesWithAnnotation(Class<? extends Annotation> annotationType) {
        if (annotationType == null) {
            throw new IllegalArgumentException("annotationType must not be null");
        }
    }
}
