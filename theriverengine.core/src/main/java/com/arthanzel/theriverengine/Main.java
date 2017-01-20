package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.adminui.AdminUI;
import com.arthanzel.theriverengine.gui.RiverView;
import com.arthanzel.theriverengine.reporting.FileReporter;
import com.arthanzel.theriverengine.reporting.RiverReporter;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.sim.RiverRunner;
import com.arthanzel.theriverengine.sim.RiverSystem;
import javafx.application.Application;
import javafx.stage.Stage;
import com.arthanzel.theriverengine.sim.influence.*;

import java.io.File;

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
//        main.show();
//        Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // UI thread
//
//        runner.start();
    @Override
    public void start(Stage primaryStage) throws Exception {
        RiverNetwork network = RiverNetwork.fromResource("/graphs/binarytree-3.ini");
        RiverSystem system = new RiverSystem(network, 100);
        RiverRunner runner = new RiverRunner(system);

        // region Influences
        Influence flowInf = new FlowMovement();
        flowInf.setEnabled(true);
        runner.getInfluences().add(flowInf);
        // endregion

        // Reporter
        RiverReporter reporter = new RiverReporter(runner);
        reporter.getConsumers().add(s -> {
            System.out.println(runner.getOptions().getQueueMode());
        });
        reporter.getConsumers().add(new FileReporter(new File("results.txt")));

        // GUI
        RiverView view = new RiverView();
        view.show();
        reporter.getConsumers().add(s -> {
            view.updateModel(s);
        });

        reporter.start();

        runner.start();
        runner.setEnabled(true);

        // region Admin UI
        AdminUI adminUI = new AdminUI(runner);
        // TODO: Wire root controls
        adminUI.setOnCloseRequest(event -> {
            System.exit(0);
        });
        // endregion

        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
