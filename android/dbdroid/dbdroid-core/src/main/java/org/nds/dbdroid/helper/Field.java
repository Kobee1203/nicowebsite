package org.nds.dbdroid.helper;

import java.lang.annotation.Annotation;
import java.util.Set;

public class Field {

    private String fieldName;
    private Object fieldValue;
    private Set<Annotation> fieldAnnotations;
    private Class<?> fieldType;

    public Field(String fieldName, Object fieldValue, Set<Annotation> fieldAnnotations, Class<?> fieldType) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.fieldAnnotations = fieldAnnotations;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public Set<Annotation> getFieldAnnotations() {
        return fieldAnnotations;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[fieldName: " + this.fieldName + ", ");
        sb.append("fieldValue: " + this.fieldValue + ", ");
        sb.append("fieldAnnotations: " + this.fieldAnnotations + ", ");
        sb.append("fieldType: " + (this.fieldType != null ? this.fieldType.getCanonicalName() : null) + "]");
        return sb.toString();
    }
}
