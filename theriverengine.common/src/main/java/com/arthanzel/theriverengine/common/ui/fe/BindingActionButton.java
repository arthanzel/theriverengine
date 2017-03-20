package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.scene.control.Button;

import java.lang.reflect.Field;

/**
 * Created by martin on 2017-03-05.
 */
public class BindingActionButton extends FieldEditor<Integer> {
    public BindingActionButton(Field f, Object bean) throws BindingInvocationException, TypeMismatchException {
        super(f, bean, Integer.class);

        Button btn = new Button(ReflectionUtils.getBoundName(f));
        btn.setOnAction(event -> {
            this.setValue(this.getValue() + 1);
        });

        this.getChildren().add(btn);
    }
}
