package org.nds.dbdroid.reflect.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ReflectUtils {

    public static <T> List<Constructor<?>> getConstructors(Class<T> clazz, boolean recursive) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();

        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (final Constructor<?> constructor : declaredConstructors) {
            // need to avoid the Object constructor
            if (!Object.class.equals(clazz) && constructor != null) {
                int modifiers = constructor.getModifiers();
                if (Modifier.isPublic(modifiers)) {
                    constructors.add(constructor);
                } else {
                    try {
                        AccessController.doPrivileged(new PrivilegedAction<T>() {
                            public T run() {
                                constructor.setAccessible(true);
                                return null;
                            }
                        });
                        constructors.add(constructor);
                    } catch (SecurityException e) {
                        // oh well, this does not get added then
                    }
                }
            }
        }

        if (recursive) {
            List<Class<?>> interfaces = getInterfaces(clazz, recursive);
            for (Class<?> i : interfaces) {
                constructors.addAll(getConstructors(i, recursive));
            }

            List<Class<?>> superclasses = getSuperclasses(clazz, recursive);
            for (Class<?> superclass : superclasses) {
                constructors.addAll(getConstructors(superclass, recursive));
            }
        }

        return constructors;
    }

    public static List<Class<?>> getInterfaces(Class<?> clazz, boolean recursive) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        Set<Class<?>> interfaces = new LinkedHashSet<Class<?>>();
        for (Class<?> i : clazz.getInterfaces()) {
            interfaces.add(i);
        }

        if (recursive) {
            for (Class<?> i : interfaces) {
                interfaces.addAll(getInterfaces(i, recursive));
            }
        }

        return new ArrayList<Class<?>>(interfaces);
    }

    public static List<Class<?>> getSuperclasses(Class<?> clazz, boolean recursive) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        List<Class<?>> superclasses = new ArrayList<Class<?>>();
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            superclasses.add(superclass);

            if (recursive) {
                superclasses.addAll(getSuperclasses(superclass, recursive));
            }
        }

        return superclasses;
    }

    public static <T> List<Field> getFields(Class<T> clazz, boolean recursive, boolean includeNonPublic) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        List<Field> fields = new ArrayList<Field>();

        Field[] declaredFields = clazz.getDeclaredFields();
        for (final Field field : declaredFields) {
            if (field != null) {
                int modifiers = field.getModifiers();
                if (Modifier.isPublic(modifiers)) {
                    fields.add(field);
                } else {
                    try {
                        AccessController.doPrivileged(new PrivilegedAction<T>() {
                            public T run() {
                                field.setAccessible(true);
                                return null;
                            }
                        });
                        fields.add(field);
                    } catch (SecurityException e) {
                        // oh well, this does not get added then
                    }
                }
            }
        }

        if (recursive) {
            List<Class<?>> interfaces = getInterfaces(clazz, recursive);
            for (Class<?> i : interfaces) {
                fields.addAll(getFields(i, recursive, includeNonPublic));
            }

            List<Class<?>> superclasses = getSuperclasses(clazz, recursive);
            for (Class<?> superclass : superclasses) {
                fields.addAll(getFields(superclass, recursive, includeNonPublic));
            }
        }

        return fields;
    }

    public static <T> List<Method> getMethods(Class<T> clazz, boolean recursive, boolean includeNonPublic) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null");
        }

        List<Method> methods = new ArrayList<Method>();

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (final Method method : declaredMethods) {
            if (method != null) {
                int modifiers = method.getModifiers();
                if (Modifier.isPublic(modifiers)) {
                    methods.add(method);
                } else {
                    try {
                        AccessController.doPrivileged(new PrivilegedAction<T>() {
                            public T run() {
                                method.setAccessible(true);
                                return null;
                            }
                        });
                        methods.add(method);
                    } catch (SecurityException e) {
                        // oh well, this does not get added then
                    }
                }
            }
        }

        if (recursive) {
            List<Class<?>> interfaces = getInterfaces(clazz, recursive);
            for (Class<?> i : interfaces) {
                methods.addAll(getMethods(i, recursive, includeNonPublic));
            }

            List<Class<?>> superclasses = getSuperclasses(clazz, recursive);
            for (Class<?> superclass : superclasses) {
                methods.addAll(getMethods(superclass, recursive, includeNonPublic));
            }
        }

        return methods;
    }

}
