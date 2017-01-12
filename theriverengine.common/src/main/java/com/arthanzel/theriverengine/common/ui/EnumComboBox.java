package com.arthanzel.theriverengine.common.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ComboBox;

/**
 * Automatically constructs
 */
public class EnumComboBox<T extends Enum> extends ComboBox<T> {
    private ObjectProperty<Class<T>> enumClass = new SimpleObjectProperty<>();

    public EnumComboBox() {
        enumClass.addListener((observable, oldValue, newValue) -> setItemsFromEnum());
    }

    public EnumComboBox(Class<T> theEnumClass) {
        this();
        enumClass.set(theEnumClass);
    }

    public void select(T val) {
        this.getSelectionModel().select(val);
    }

    private void setItemsFromEnum() {
        try {
            T[] items = enumClass.get().getEnumConstants();
            this.getItems().setAll(items);
            this.getSelectionModel().selectFirst();
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Bad enum");
        }
    }

    public Class getEnumClass() {
        return enumClass.get();
    }

    public ObjectProperty<Class<T>> enumClassProperty() {
        return enumClass;
    }

    public void setEnumClass(Class enumClass) {
        this.enumClass.set(enumClass);
    }
}
