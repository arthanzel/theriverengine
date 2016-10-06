package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.ui.RiverView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point for The River Engine.
 *
 * @author Martin
 */
public class Main extends Application {
    @Override
    public void start(Stage main) throws IOException {
        Stage view = new RiverView(RiverNetwork.fromResource("/graphs/binarytree-3.ini"));
        view.show();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
