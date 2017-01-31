package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.lang.reflect.Field;

/**
 * DisplayableField displays the value of a field without any ability to edit.
 *
 * @author Martin
 */
public class EnumComboField extends FieldEditor<Enum> {
    public <K extends Enum> EnumComboField(Field field, Object bean, Class<K> enumClass) throws BindingInvocationException, TypeMismatchException {
        super(field, bean, enumClass);

        Class cls = this.getType();
        assert cls.isEnum();

        ComboBox<Enum> comboBox = new ComboBox<>();
        comboBox.valueProperty().bindBidirectional(this.valueProperty());
        comboBox.getItems().setAll((Enum[]) cls.getEnumConstants());
        this.getChildren().addAll(
                new Label(ReflectionUtils.getBoundName(field) + ": "),
                comboBox
        );
    }
}
