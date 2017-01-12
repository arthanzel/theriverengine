package com.arthanzel.theriverengine.common.ui.binding;

import com.arthanzel.theriverengine.common.ui.fe.BindingInvocationException;
import com.arthanzel.theriverengine.common.ui.fe.DisplayableField;
import com.arthanzel.theriverengine.common.ui.fe.FileField;
import com.arthanzel.theriverengine.common.ui.fe.TypeMismatchException;
import javafx.scene.Node;

import java.lang.reflect.Field;

/**
 * Utility class that provides methods for creating UI controls for field bindings.
 */
public class Bindings {
    private Bindings() {}

    public static Node createFor(Field f, Object bean) throws BindingInvocationException, TypeMismatchException {
        if (f.isAnnotationPresent(DisplayableBinding.class)) {
            return new DisplayableField<Object>(f, bean, f.getAnnotation(DisplayableBinding.class));
        }
        if (f.isAnnotationPresent(FileBinding.class)) {
            return new FileField(f, bean, f.getAnnotation(FileBinding.class));
        }
        return null;
    }
}
