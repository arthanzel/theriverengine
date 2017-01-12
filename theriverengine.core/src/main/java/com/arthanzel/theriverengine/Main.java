package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.adminui.AdminUI;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.sim.RiverRunner;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.environment.DiscreteEnvironment;
import com.arthanzel.theriverengine.sim.environment.TemperatureEnvironment;
import com.arthanzel.theriverengine.ui.RiverViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.arthanzel.theriverengine.sim.influence.*;

import java.io.IOException;

/**
 * Entry point for The River Engine.
 *
 * @author Martin
 */
public class Main extends Application {
//    public void start(Stage main) throws Exception {
//        TheRiverEngineApplication

//        String windowTitle = "The River Engine - Test UI";
//        main.setTitle(windowTitle);
//
//        // Create the system and add Environments to the data model
//        RiverSystem system = new RiverSystem(RiverNetwork.fromResource("/graphs/binarytree-3.ini"), 800);
//        system.getEnvironments().put("temperature", new TemperatureEnvironment());
//        system.getEnvironments().put("nutrients", new DiscreteEnvironment(system.getNetwork()));
//
//        // Create the runner and add Influences to change behaviour.
//        RiverRunner runner = new RiverRunner(system);
//        runner.getInfluences().add(new RandomMovement());
//        runner.getInfluences().add(new FlowMovement());
//        runner.getInfluences().add(new VelocityApplier());
//
//        Influence nutrientDynamics = new NutrientDynamics();
//        nutrientDynamics.setEnabled(false);
//        runner.getInfluences().add(nutrientDynamics);
//
//        Influence nutrientDynamicsLog = new NutrientDynamicsLog();
//        nutrientDynamicsLog.setEnabled(false);
//        runner.getInfluences().add(nutrientDynamicsLog);
//
//        Influence feedingInfluence = new FeedingInfluence();
//        feedingInfluence.setEnabled(false);
//        runner.getInfluences().add(feedingInfluence);
//
//        Influence deathDynamics = new DeathDynamics();
//        deathDynamics.setEnabled(false);
//        runner.getInfluences().add(deathDynamics);
//
//        Influence reproductionDynamics = new ReproductionDynamics(system);
//        runner.getInfluences().add(reproductionDynamics);
//
//        // Message queue
//        Thread consumer = new Thread(() -> {
//            while (true) {
//                try {
//                    String msg = runner.getMessageQueue().take();
//                    int i = 0;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        consumer.start();
//
//        // Load UI
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RiverView.fxml"));
//        Scene scene = new Scene(loader.load(), 800, 600);
//        RiverViewController controller = loader.getController();
//        main.setScene(scene);
//        controller.initialize(system.clone(), runner);
//        controller.speedProperty().addListener((observable, oldValue, newValue) -> {
//            runner.setInterval((double) newValue);
//        });
//        controller.playingProperty().addListener((observable, oldValue, newValue) -> {
//            runner.setEnabled(newValue);
//        });
//        controller.setOnForward(event -> runner.forward());
//
//        main.setOnCloseRequest(event -> {
//            System.out.println("Exiting");
//            main.close();
//        });
//
//        runner.setRefreshHandler(controller::setSystem);
//
//        main.show();
//        Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // UI thread
//
//        runner.start();
    @Override
    public void start(Stage primaryStage) throws Exception {
        RiverNetwork network = RiverNetwork.fromResource("/graphs/binarytree-3.ini");
        RiverSystem system = new RiverSystem(network, 100);
        RiverRunner runner = new RiverRunner(system);

        AdminUI adminUI = new AdminUI(runner);

        runner.start();
        runner.setEnabled(true);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
