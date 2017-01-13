package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.ui.DynamicLabel;
import com.arthanzel.theriverengine.common.ui.binding.DisplayableBinding;
import com.arthanzel.theriverengine.common.ui.binding.EnumBinding;
import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;

import java.lang.reflect.Field;

/**
 * DisplayableField displays the value of a field without any ability to edit.
 *
 * @author Martin
 */
public class EnumComboField extends FieldEditor<Object> {
    public EnumComboField(Field field, Object bean, EnumBinding annotation) throws BindingInvocationException {
        super(field, bean);

        Class cls = this.getType();
        assert cls.isEnum();

        ComboBox<Object> comboBox = new ComboBox<>();
        comboBox.valueProperty().bindBidirectional(this.valueProperty());
        comboBox.getItems().setAll(cls.getEnumConstants());
        this.getChildren().addAll(
                new Label(ReflectionUtils.getBoundName(field) + ": "),
                comboBox
        );
    }
}
