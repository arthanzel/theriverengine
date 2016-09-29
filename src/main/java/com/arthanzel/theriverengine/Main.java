package com.arthanzel.theriverengine;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point for The River Engine.
 *
 * @author Martin
 */
public class Main extends Application {
    @Override
    public void start(Stage main) {
        main.setTitle("The River Engine");
        main.show();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
