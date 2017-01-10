package com.arthanzel.theriverengine.ui.controls;

import com.arthanzel.theriverengine.util.ReflectionUtils;
import javafx.scene.control.CheckBox;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * The BooleanValueEditor is a control that displays and modifies some boolean property of a bean. Changes are immediately
 * propagated to the underlying bean.
 *
 * @author Martin
 */
public class BooleanValueEditor extends BeanValueEditor<Boolean> {
    public BooleanValueEditor(Field field, Object bean) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        super(field, bean);

        boolean val = this.getValue();
        CheckBox checkbox = new CheckBox(ReflectionUtils.getBeanName(field));
        checkbox.setSelected(val);
        this.getChildren().add(checkbox);

        checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                this.setValue(newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
