package com.arthanzel.theriverengine.common.util;

import com.arthanzel.theriverengine.common.ui.binding.BindingName;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides helper methods for dealing with reflection and introspection.
 *
 * @author Martin
 */
public class ReflectionUtils {
    /**
     * Returns a list of all Fields, public or otherwise, that are declared in a class or its superclasses.
     * Fields are sorted by their declaring class. Fields that are declared by the given class come first, while those
     * defined in superclasses appear later in the list.
     *
     * @param cls A class whose fields are to be retrieved.
     * @return List of Field.
     */
    public static List<Field> getAllDeclaredFields(Class cls) {
        List<Field> fields = new ArrayList<>();
        while (cls != null) {
            fields.addAll(Arrays.asList(cls.getDeclaredFields()));
            cls = cls.getSuperclass();
        }
        return fields;
    }

    /**
     * Gets the name of the bound bean.
     * If the bean has a BindingName annotation, this is the annotation's value.
     * If the 'bean' is a field, this method take its name split up into multiple words.
     * Else, this method takes the simple class name split up into multiple words.
     *
     * @param obj The bean.
     * @return The bean's bound name, or computed name.
     */
    public static String getBoundName(Object obj) {
        if (obj instanceof Field) {
            Field f = (Field) obj;
            if (f.isAnnotationPresent(BindingName.class)) {
                return f.getDeclaredAnnotation(BindingName.class).value();
            }
            return TextUtils.toWords(f.getName());
        }
        else {
            if (obj.getClass().isAnnotationPresent(BindingName.class)) {
                return obj.getClass().getAnnotation(BindingName.class).value();
            }
            return TextUtils.toWords(obj.getClass().getSimpleName());
        }
    }

    /**
     * Returns the method for accessing a JavaFX property. Assumes that the
     * method is named by the default naming convention:
     * <code>(name)Property</code>. Returns null if no such method exists.
     *
     * @param name Name of the property.
     * @param cls  Class containing the property.
     */
    public static Method getPropertyMethod(String name, Class<?> cls) {
        try {
            return cls.getMethod(name + "Property");
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * From Ini4j. Converts a primitive value encoded in a String into its associated reference type.
     *
     * @param value String encoding primitive value.
     * @param clazz Class to which to convert the string.
     * @throws IllegalArgumentException Thrown if clazz is not a primitive wrapper or if the string does not encode a proper value.
     */
    public static Object parsePrimitiveValue(String value, Class clazz) throws IllegalArgumentException {
        try {
            if (clazz == Boolean.TYPE) {
                return Boolean.valueOf(value);
            }
            else if (clazz == Byte.TYPE) {
                return Byte.valueOf(value);
            }
            else if (clazz == Character.TYPE) {
                return value.charAt(0);
            }
            else if (clazz == Double.TYPE) {
                return Double.valueOf(value);
            }
            else if (clazz == Float.TYPE) {
                return Float.valueOf(value);
            }
            else if (clazz == Integer.TYPE) {
                return Integer.valueOf(value);
            }
            else if (clazz == Long.TYPE) {
                return Long.valueOf(value);
            }
            else if (clazz == Short.TYPE) {
                return Short.valueOf(value);
            }
            else if (clazz == String.class) {
                return value;
            }
            else {
                throw new IllegalArgumentException("Class must be a primitive wrapper class.");
            }
        }
        catch (Exception x) {
            throw new IllegalArgumentException(x);
        }
    }
}
