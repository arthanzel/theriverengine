package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.sim.RiverRunner;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.environment.TemperatureEnvironment;
import com.arthanzel.theriverengine.sim.influence.*;
import com.arthanzel.theriverengine.ui.RiverViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        main.setTitle("The River Engine - Test UI");

        RiverSystem system = new RiverSystem(RiverNetwork.fromResource("/graphs/binarytree-3.ini"), 100);
        system.getEnvironments().put("temperature", new TemperatureEnvironment());

        RiverRunner runner = new RiverRunner(system);
        runner.getInfluences().add(new RandomMovement());
        runner.getInfluences().add(new FlowMovement());
        runner.getInfluences().add(new VelocityApplier());
        Influence nutrientDynamics = new NutrientDynamics();
        nutrientDynamics.setEnabled(false);
        runner.getInfluences().add(nutrientDynamics);
        Influence nutrientDynamicsLog = new NutrientDynamicsLog();
        nutrientDynamicsLog.setEnabled(false);
        runner.getInfluences().add(nutrientDynamicsLog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RiverView.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);
        main.setScene(scene);
        loader.<RiverViewController>getController().initialize(system, runner);

        main.setOnCloseRequest(event -> {
            System.out.println("Exiting");
            System.exit(0);
        });

        main.show();
        runner.start();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
