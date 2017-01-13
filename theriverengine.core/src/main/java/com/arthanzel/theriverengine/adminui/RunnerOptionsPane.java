package com.arthanzel.theriverengine.adminui;

import com.arthanzel.theriverengine.common.ui.binding.Bindings;
import com.arthanzel.theriverengine.common.ui.fe.BindingInvocationException;
import com.arthanzel.theriverengine.common.ui.fe.TypeMismatchException;
import com.arthanzel.theriverengine.concurrent.QueueMode;
import com.arthanzel.theriverengine.sim.RunnerOptions;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Created by martin on 2017-01-10.
 */
public class RunnerOptionsPane extends TitledPane {
    @FXML
    RunnerOptions model;

    public RunnerOptionsPane(RunnerOptions options) {
        this.model = options;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RunnerOptionsPane.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load RunnerOptionsPane.fxml");
        }

        try {
            VBox vbox = new VBox(5);
            vbox.getChildren().addAll(Bindings.createForBean(model));
            this.setContent(vbox);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
    }
}
