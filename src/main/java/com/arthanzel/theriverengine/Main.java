package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.sim.RiverSystem;
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
        TestUI tui = new TestUI();
        tui.show();

//        RiverSystem system = new RiverSystem(RiverNetwork.fromResource("/graphs/binarytree-3.ini"), 50);
//        system.initAgentsRandomly();
//        RiverView view = new RiverView(system);
//        view.show();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
