package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.ui.RiverViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class TestUI extends Stage {
    public TestUI() throws IOException {
        this.setTitle("The River Engine - Test UI");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RiverView.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        this.setScene(scene);
    }
}
