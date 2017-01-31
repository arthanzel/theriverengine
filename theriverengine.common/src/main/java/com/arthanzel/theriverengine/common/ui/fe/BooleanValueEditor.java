package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.scene.control.CheckBox;

import java.lang.reflect.Field;

/**
 * The BooleanValueEditor is a control that displays and modifies some boolean property of a bean. Changes are immediately
 * propagated to the underlying bean.
 *
 * @author Martin
 */
public class BooleanValueEditor extends FieldEditor<Boolean> {
    public BooleanValueEditor(Field field, Object bean) throws BindingInvocationException, TypeMismatchException {
        super(field, bean, Boolean.class);

        CheckBox checkbox = new CheckBox(ReflectionUtils.getBoundName(field));
        checkbox.setSelected(this.getValue());
        this.getChildren().add(checkbox);
        checkbox.selectedProperty().bindBidirectional(this.valueProperty());
    }
}
