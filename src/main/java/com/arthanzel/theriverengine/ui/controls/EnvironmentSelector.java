package com.arthanzel.theriverengine.ui.controls;

import com.arthanzel.theriverengine.sim.environment.Environment;
import com.arthanzel.theriverengine.util.TextUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.commons.math3.stat.inference.TestUtils;

import java.util.Map;

/**
 * Created by martin on 2016-11-02.
 */
public class EnvironmentSelector extends VBox {
    private ObjectProperty<Environment> selectedEnvironment = new SimpleObjectProperty<>();

    public EnvironmentSelector(Map<String, Environment> environments) {
        super(7);

        ToggleGroup group = new ToggleGroup();

        RadioButton noneRB = new RadioButton("None");
        noneRB.setSelected(true);
        noneRB.setToggleGroup(group);
        this.getChildren().add(noneRB);
        noneRB.selectedProperty().addListener((observable, oldValue, newValue) -> {
            selectedEnvironment.set(null);
        });


        for (String s : environments.keySet()) {
            Environment env = environments.get(s);
            RadioButton rb = new RadioButton(TextUtils.toWords(s));
            rb.setToggleGroup(group);
            this.getChildren().add(rb);
            rb.selectedProperty().addListener((observable, wasSelected, selected) -> {
                if (selected) {
                    selectedEnvironment.set(env);
                }
            });
        }
    }

    public Environment getSelectedEnvironment() {
        return selectedEnvironment.get();
    }

    public ObjectProperty<Environment> selectedEnvironmentProperty() {
        return selectedEnvironment;
    }

    public void setSelectedEnvironment(Environment selectedEnvironment) {
        this.selectedEnvironment.set(selectedEnvironment);
    }
}
