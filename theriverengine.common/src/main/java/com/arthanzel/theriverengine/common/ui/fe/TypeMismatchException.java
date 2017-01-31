package com.arthanzel.theriverengine.common.ui.fe;

import java.lang.reflect.Field;

/**
 * Thrown when a FieldEditor is bound on a field with a different type.
 */
public class TypeMismatchException extends Exception {
    public TypeMismatchException(String msg) {
        super(msg);
    }

    // TODO: This message should apply for sets as well as creates.
    public TypeMismatchException(Field field) {
        super("Tried to create a FieldEditor on " + field.getName()
                + " that doesn't match type " + field.getType());
    }
}
