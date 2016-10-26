package com.arthanzel.theriverengine.ui.controls;

import com.arthanzel.theriverengine.ui.DoubleBinding;
import com.arthanzel.theriverengine.util.ReflectionUtils;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * The BooleanValueEditor is a control that displays and modifies some double property of a bean (representing a real
 * number). Changes are immediately propagated to the underlying bean.
 *
 * @author Martin
 */
public class RealValueEditor extends BeanValueEditor<Double> {
    public RealValueEditor(Field field, Object bean, DoubleBinding annotation) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        super(field, bean);

        double val = this.getValue();

        DynamicLabel label = new DynamicLabel(ReflectionUtils.getBeanName(field), val);
        this.getChildren().add(label);

        Slider slider = new Slider(annotation.min(), annotation.max(), val);
        this.getChildren().add(slider);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                this.setValue(newValue.doubleValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        label.valueProperty().bind(slider.valueProperty());
    }
}
