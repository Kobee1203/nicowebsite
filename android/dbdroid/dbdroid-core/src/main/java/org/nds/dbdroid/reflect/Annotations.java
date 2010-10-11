package org.nds.dbdroid.reflect;

import java.lang.annotation.Annotation;
import java.util.List;

public class Annotations {

    private final List<Annotation> classAnnotations;
    private final List<Annotation> constructorsAnnotations;
    private final List<Annotation> fieldsAnnotations;
    private final List<Annotation> methodsAnnotations;

    public Annotations(Class<?> clazz, boolean recursive, boolean includeNonPublic) {
        // TODO Auto-generated constructor stub
    }
}
