package com.arthanzel.theriverengine.ui.controls;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class DynamicLabel extends Label {
    private StringProperty name = new SimpleStringProperty("");
    private DoubleProperty value = new SimpleDoubleProperty(0);

    public DynamicLabel(String name, double val) {
        setName(name);
        setValue(val);
        setText();

        nameProperty().addListener((observable, oldValue, newValue) -> {
            setText();
        });
        valueProperty().addListener((observable, oldValue, newValue) -> {
            setText();
        });
    }

    private void setText() {
        setText(getName() + " (" + String.format("%.2f", getValue()) + ")");
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getValue() {
        return value.get();
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public void setValue(double value) {
        this.value.set(value);
    }
}
