package com.arthanzel.theriverengine.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides helper methods for dealing with reflection and introspection.
 *
 * @author Martin
 */
public class ReflectionUtils {
    private ReflectionUtils() {
    }

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
}
