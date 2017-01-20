package com.arthanzel.theriverengine.gui;


import com.arthanzel.theriverengine.common.ui.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

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

    private RenderOptions options;

    public RiverView() {
        options = new RenderOptions();
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
        controlsContainer.getChildren().addAll(Bindings.createForBean(options));
    }

    public void updateModel(String json) {
        if (riverRenderer != null) {
            riverRenderer.update(json);
        }
    }

    public RenderOptions getOptions() {
        return options;
    }
}
