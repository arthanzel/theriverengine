package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.sim.RiverSystem;
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

        RiverSystem system = new RiverSystem(RiverNetwork.fromResource("/graphs/binarytree-3.ini"), 50);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RiverView.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        loader.<RiverViewController>getController().setSystem(system);
        this.setScene(scene);
    }
}
