package com.arthanzel.theriverengine.common.ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 * Text input component that exposes a user's input as a number.
 */
public class NumberField extends TextField {
    private DoubleProperty value = new SimpleDoubleProperty();

    /**
     * Creates a new NumberField set to zero.
     */
    public NumberField() {
        this(0);
    }

    /**
     * Creates a new NumberField set to a default value.
     * @param defaultValue Default value.
     */
    public NumberField(double defaultValue) {
        value.set(defaultValue);

        this.focusedProperty().addListener((observable, oldValue, focused) -> {
            if (!focused) {
                apply();
            }
        });

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                apply();
                this.getParent().requestFocus();
            }
        });

        value.addListener((observable, oldValue, newValue) -> {
            this.setText(newValue.toString());
        });
    }

    private void apply() {
        try {
            double d = Double.parseDouble(this.getText());
            if (!Double.isFinite(d)) {
                // Prevent infinities or NaN
                throw new Exception();
            }
            value.set(d);
        }
        catch (Exception e) {
            // Can't parse double, so revert to the previous value.
            this.setText(Double.toString(getValue()));
        }
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
