package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.ui.binding.DoubleSpinnerBinding;
import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

import java.lang.reflect.Field;

/**
 *
 *
 * @author Martin
 */
public class DoubleSpinnerField extends FieldEditor<Double> {
    public DoubleSpinnerField(Field field, Object bean) throws BindingInvocationException, TypeMismatchException {
        super(field, bean, Double.class);

        DoubleSpinnerBinding annotation = field.getAnnotation(DoubleSpinnerBinding.class);
        Spinner<Double> spinner = new Spinner<>(annotation.min(), annotation.max(), this.getValue(), annotation.step());
        spinner.setEditable(true);
        spinner.getValueFactory().valueProperty().bindBidirectional(this.valueProperty());
        this.getChildren().addAll(
                new Label(ReflectionUtils.getBoundName(field) + ": "),
                spinner
        );
    }
}
