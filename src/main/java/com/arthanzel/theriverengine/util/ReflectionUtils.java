package com.arthanzel.theriverengine.util;

import com.arthanzel.theriverengine.ui.BindingName;

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

    /**
     * Gets the name of the bound bean.
     * If the bean has a BindingName annotation, this is the annotation's value.
     * If the 'bean' is a field, this method take its name split up into multiple words.
     * Else, this method takes the simple class name split up into multiple words.
     * @param obj The bean.
     * @return The bean's bound name, or computed name.
     */
    public static String getBeanName(Object obj) {
        if (obj.getClass().isAnnotationPresent(BindingName.class)) {
            return obj.getClass().getAnnotation(BindingName.class).name();
        }
        else if (obj instanceof Field) {
            Field f = (Field) obj;
            return TextUtils.toWords(f.getName());
        }
        else {
            return TextUtils.toWords(obj.getClass().getSimpleName());
        }
    }
}
