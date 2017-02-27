package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.ui.DynamicLabel;
import com.arthanzel.theriverengine.common.ui.NumberPrompt;
import com.arthanzel.theriverengine.common.ui.fe.BindingInvocationException;
import com.arthanzel.theriverengine.common.ui.fe.FieldEditor;
import com.arthanzel.theriverengine.common.ui.fe.TypeMismatchException;
import com.arthanzel.theriverengine.common.ui.binding.SliderBinding;
import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.lang.reflect.Field;

/**
 * The BooleanValueEditor is a control that displays and modifies some double property of a bean (representing a real
 * number). Changes are immediately propagated to the underlying bean.
 *
 * @author Martin
 */
public class RealValueEditor extends FieldEditor<Double> {
    public RealValueEditor(Field field, Object bean) throws BindingInvocationException, TypeMismatchException {
        super(field, bean, Double.class);
        SliderBinding annotation = field.getAnnotation(SliderBinding.class);

        double val = this.getValue();
        String name = ReflectionUtils.getBoundName(field);

        DynamicLabel<Double> label = new DynamicLabel<>(name + " (%.3f)", val);
        this.getChildren().add(label);

        Slider slider = new Slider(annotation.min(), annotation.max(), val);
        this.getChildren().add(slider);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                this.setValue(newValue.doubleValue());
                label.setValue(newValue.doubleValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Show a dialog on double click
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() != 2) {
                return;
            }

            NumberPrompt np = new NumberPrompt("Enter a value for '" + name + "'");
            np.initOwner(this.getScene().getWindow());
            np.setSubmitHandler(ev -> {
                try {
                    slider.setValue((double) ev.getSource());
                    this.setValue((double) ev.getSource());
                    label.setValue((double) ev.getSource());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            np.show();
        });
    }
}
