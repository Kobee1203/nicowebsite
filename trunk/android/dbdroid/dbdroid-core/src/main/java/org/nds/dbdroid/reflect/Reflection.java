package org.nds.dbdroid.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.nds.dbdroid.reflect.utils.ReflectUtils;

public class Reflection {

    private final Class<?> clazz;
    private final boolean includeNonPublic;
    private final boolean recursive;

    private final List<Class<?>> interfaces;
    private final List<Class<?>> superclasses;
    private final List<Constructor<?>> constructors;
    private final List<Field> fields;
    private final List<Method> methods;
    private final List<Annotation> classAnnotations;
    private final List<Annotation> constructorsAnnotations;
    private final List<Annotation> fieldsAnnotations;
    private final List<Annotation> methodsAnnotations;

    public Reflection(Class<?> clazz) {
        this(clazz, true, true);
    }

    public Reflection(Class<?> clazz, boolean recursive, boolean includeNonPublic) {
        this.clazz = clazz;
        this.recursive = recursive;
        this.includeNonPublic = includeNonPublic;

        interfaces = ReflectUtils.getInterfaces(clazz, recursive);
        superclasses = ReflectUtils.getSuperclasses(clazz, recursive);
        constructors = retrieveConstructors();
        fields = retrieveFields();
        methods = retrieveMethods();
        classAnnotations = retrieveClassAnnotations();
        constructorsAnnotations = retrieveConstructorsAnnotations();
        fieldsAnnotations = retrieveFieldsAnnotations();
        methodsAnnotations = retrieveMethodsAnnotations();
    }

    public List<Class<?>> getInterfaces() {
        return interfaces;
    }

    public List<Class<?>> getSuperclasses() {
        return superclasses;
    }

    private List<Constructor<?>> retrieveConstructors() {
        List<Constructor<?>> constructors = ReflectUtils.getConstructors(clazz, false);

        if (recursive) {
            for (Class<?> i : getInterfaces()) {
                constructors.addAll(ReflectUtils.getConstructors(i, recursive));
            }

            for (Class<?> superclass : getSuperclasses()) {
                constructors.addAll(ReflectUtils.getConstructors(superclass, recursive));
            }
        }

        return constructors;
    }

    public List<Constructor<?>> getConstructors() {
        return constructors;
    }

    private List<Field> retrieveFields() {
        List<Field> fields = ReflectUtils.getFields(clazz, false, includeNonPublic);

        if (recursive) {
            for (Class<?> i : getInterfaces()) {
                fields.addAll(ReflectUtils.getFields(i, recursive, includeNonPublic));
            }

            for (Class<?> superclass : getSuperclasses()) {
                fields.addAll(ReflectUtils.getFields(superclass, recursive, includeNonPublic));
            }
        }

        return fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    private List<Method> retrieveMethods() {
        List<Method> methods = ReflectUtils.getMethods(clazz, false, includeNonPublic);

        if (recursive) {
            for (Class<?> i : getInterfaces()) {
                methods.addAll(ReflectUtils.getMethods(i, recursive, includeNonPublic));
            }

            for (Class<?> superclass : getSuperclasses()) {
                methods.addAll(ReflectUtils.getMethods(superclass, recursive, includeNonPublic));
            }
        }

        return methods;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<Annotation> getAnnotations() {
        List<Annotation> annotations = new ArrayList<Annotation>();
        // Add all class annotations
        annotations.addAll(getClassAnnotations(clazz));
        // Add all fields annotations
        annotations.addAll(getFieldsAnnotations(clazz, recursive, includeNonPublic));
        // Add all methods annotations
        annotations.addAll(getMethodsAnnotations(clazz, recursive, includeNonPublic));

        return annotations;
    }
}
