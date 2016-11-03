package com.arthanzel.theriverengine.ui.controls;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;

/**
 * Created by martin on 2016-11-02.
 */
public class IntegerTextField extends TextField {
    public IntegerTextField() {
        this.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
    }
}
