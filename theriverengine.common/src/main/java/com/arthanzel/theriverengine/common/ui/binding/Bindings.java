package com.arthanzel.theriverengine.common.ui.binding;

import com.arthanzel.theriverengine.common.ui.fe.*;
import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.scene.Node;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class that provides methods for creating UI controls for field bindings.
 */
public class Bindings {
    private Bindings() {}

    public static Node createFor(Field f, Object bean) throws BindingInvocationException {
        if (f.isAnnotationPresent(DisplayableBinding.class)) {
            return new DisplayableField<>(f, bean, f.getAnnotation(DisplayableBinding.class));
        }
        if (f.isAnnotationPresent(FileBinding.class)) {
            return new FileField(f, bean, f.getAnnotation(FileBinding.class));
        }
        if (f.isAnnotationPresent(DoubleSpinnerBinding.class)) {
            return new DoubleSpinnerField(f, bean, f.getAnnotation(DoubleSpinnerBinding.class));
        }
        if (f.isAnnotationPresent(IntegerSpinnerBinding.class)) {
            return new IntegerSpinnerField(f, bean, f.getAnnotation(IntegerSpinnerBinding.class));
        }
        if (f.isAnnotationPresent(EnumBinding.class)) {
            return new EnumComboField(f, bean, f.getAnnotation(EnumBinding.class));
        }
        return null;
    }

    public static List<Node> createForBean(Object bean) {
        List<Node> nodes = new LinkedList<>();
        for (Field f : ReflectionUtils.getAllDeclaredFields(bean.getClass())) {
            try {
                Node n = createFor(f, bean);
                if (n != null) {
                    nodes.add(n);
                }
            }
            catch (BindingInvocationException e) {
                // Do nothing
            }
        }
        return nodes;
    }
}
