package com.arthanzel.theriverengine.common.ui.fe;

import java.lang.reflect.Field;

/**
 * Thrown when a FieldEditor cannot find or invoke accessor methods for an
 * underlying Field.
 */
public class BindingInvocationException extends Exception {
    public BindingInvocationException(String msg) {
        super(msg);
    }

    public BindingInvocationException(Field field) {
        super("Cannot find or invoke accessor methods for field " + field.getName());
    }

    public BindingInvocationException(Field field, Class cls) {
        super("Cannot find or invoke accessor methods for field " + field.getName()
                + " on class " + cls.getTypeName());
    }
}
