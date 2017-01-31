package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.ui.DynamicLabel;
import com.arthanzel.theriverengine.common.ui.binding.DisplayableBinding;
import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.scene.control.Label;

import java.lang.reflect.Field;

/**
 * DisplayableField displays the value of a field without any ability to edit.
 *
 * @author Martin
 */
public class DisplayableField<T> extends FieldEditor<T> {
    public DisplayableField(Field field, Object bean, Class<T> template) throws BindingInvocationException, TypeMismatchException {
        super(field, bean, template);

        DisplayableBinding annotation = field.getAnnotation(DisplayableBinding.class);
        String newline = annotation.newline() ? "\n" : "";
        DynamicLabel<T> dl = new DynamicLabel<>(
                ReflectionUtils.getBoundName(field) + ": " + newline + "%s",
                valueProperty());
        this.getChildren().add(dl);
    }
}
