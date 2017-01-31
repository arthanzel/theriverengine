package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.ui.binding.DoubleSpinnerBinding;
import com.arthanzel.theriverengine.common.ui.binding.IntegerSpinnerBinding;
import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.beans.binding.IntegerBinding;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

import java.lang.reflect.Field;

/**
 *
 *
 * @author Martin
 */
public class IntegerSpinnerField extends FieldEditor<Integer> {
    public IntegerSpinnerField(Field field, Object bean) throws BindingInvocationException, TypeMismatchException {
        super(field, bean, Integer.class);

        IntegerSpinnerBinding annotation = field.getAnnotation(IntegerSpinnerBinding.class);
        Spinner<Integer> spinner = new Spinner<>(annotation.min(), annotation.max(), this.getValue(), annotation.step());
        spinner.setEditable(true);
        spinner.getValueFactory().valueProperty().bindBidirectional(this.valueProperty());
        this.getChildren().addAll(
                new Label(ReflectionUtils.getBoundName(field) + ": "),
                spinner
        );
    }
}
