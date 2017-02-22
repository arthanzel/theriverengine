package com.arthanzel.theriverengine.gui;


import com.arthanzel.theriverengine.common.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.common.ui.binding.Bindings;
import com.arthanzel.theriverengine.common.util.Benchmarks;
import com.arthanzel.theriverengine.gui.util.JsonUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverView extends Stage {
    @FXML
    private VBox controlsContainer;

    @FXML
    private RiverRenderer riverRenderer;

    @FXML
    private ComboBox<String> environmentBox;

    private RenderOptions options = new RenderOptions();
    private RiverNetwork network;

    public RiverView(RiverNetwork network) {
        this.network = network;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RiverView.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            this.setTitle("The River Engine");
            this.setScene(new Scene(root));
            this.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Can't load RiverView.fxml");
        }
    }

    public void initialize() {
        riverRenderer.setOptions(options);
        riverRenderer.updateNetworkCache(network);
        controlsContainer.getChildren().addAll(Bindings.createForBean(options));

        environmentBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            riverRenderer.setSelectedEnvironment(newValue);
        });
    }

    public void updateModel(String json) {
        JsonParser p = new JsonParser();
        JsonObject obj = p.parse(json).getAsJsonObject();

        if (riverRenderer != null) {
            riverRenderer.update(obj);
        }

        // Environment selector
        List<String> envs = JsonUtils.keys(obj.getAsJsonObject("environments"));
        envs.add(0, "No Environment");
        String currentVal = environmentBox.getValue();
        environmentBox.getItems().setAll(envs);
        environmentBox.setValue(currentVal);
    }

    public RenderOptions getOptions() {
        return options;
    }
}
