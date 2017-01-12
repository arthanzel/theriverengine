package com.arthanzel.theriverengine.adminui;

import com.arthanzel.theriverengine.common.ui.EnumComboBox;
import com.arthanzel.theriverengine.concurrent.QueueMode;
import com.arthanzel.theriverengine.sim.RiverRunner;
import com.arthanzel.theriverengine.sim.RunnerOptions;
import com.arthanzel.theriverengine.ui.controls.BeanEditPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Queue;

/**
 * AdminUI is the graphical administration console where parameters of the
 * simulation may be adjusted.
 */
public class AdminUI extends Stage {
    private RiverRunner runner;

    @FXML
    private Accordion optionsAccordion;

    public AdminUI(RiverRunner runner) throws IOException {
        this.runner = runner;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        this.setTitle("The River Engine - Admin UI");
        this.setScene(new Scene(root));
        this.show();
        EnumComboBox<QueueMode> cmb = new EnumComboBox<>(QueueMode.class);
    }

    public void initialize() {
        optionsAccordion.getPanes().add(new RunnerOptionsPane(runner.getOptions()));
    }

    public void addBean(Object bean) {
        optionsAccordion.getPanes().add(new BeanEditPane(bean));
    }
}
