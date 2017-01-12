package com.arthanzel.theriverengine.common.ui;

import javafx.beans.property.*;
import javafx.scene.control.Label;

/**
 * DynamicLabel is a Label that can display the value of some property in its
 * text. When setting the template of a DynamicLabel, use the notation provided
 * by {@link java.lang.String#format(String, Object...)} to display and format
 * the dynamic value.
 *
 * @author Martin
 */
public class DynamicLabel<T> extends Label {
    private StringProperty template = new SimpleStringProperty("");
    private ObjectProperty<T> value = new SimpleObjectProperty<>();

    /**
     * Create a DynamicLabel with a template string and initial value.
     */
    public DynamicLabel(String template, T val) {
        this.template.addListener((observable, oldValue, newValue) -> update());
        this.value.addListener((observable, oldValue, newValue) -> update());
        value.set(val);
    }

    /**
     * Create a DynamicLabel with a template string and bound to the value
     * of another property.
     */
    public DynamicLabel(String template, Property<T> property) {
        this(template, property.getValue());
        value.bind(property);
    }

    private void update() {
        super.setText(String.format(template.get(), value.get()));
    }

    public String getTemplate() {
        return template.get();
    }

    public StringProperty templateProperty() {
        return template;
    }

    public void setTemplate(String template) {
        this.template.set(template);
    }

    public T getValue() {
        return value.get();
    }

    public ObjectProperty<T> valueProperty() {
        return value;
    }

    public void setValue(T value) {
        this.value.set(value);
    }
}
