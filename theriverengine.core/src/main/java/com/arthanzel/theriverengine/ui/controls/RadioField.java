package com.arthanzel.theriverengine.ui.controls;

import com.arthanzel.theriverengine.util.TextUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

/**
 * Created by martin on 2016-11-02.
 */
public class RadioField extends VBox {
    private StringProperty selected = new SimpleStringProperty();

    public RadioField(String[] options) {
        super(7);

        ToggleGroup group = new ToggleGroup();

        RadioButton noneRB = new RadioButton("None");
        noneRB.setSelected(true);
        noneRB.setToggleGroup(group);
        this.getChildren().add(noneRB);
        noneRB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            selected.set(null);
        });


        for (String s : options) {
            RadioButton rb = new RadioButton(TextUtils.toWords(s));
            rb.setToggleGroup(group);
            this.getChildren().add(rb);
            rb.selectedProperty().addListener((observable, wasSelected, selected) -> {
                if (selected) {
                    this.selected.set(s);
                }
            });
        }
    }

    public String getSelected() {
        return selected.get();
    }

    public StringProperty selectedProperty() {
        return selected;
    }
}
