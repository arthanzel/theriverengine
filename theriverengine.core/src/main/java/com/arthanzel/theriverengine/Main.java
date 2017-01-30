package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.adminui.AdminUI;
import com.arthanzel.theriverengine.gui.RiverView;
import com.arthanzel.theriverengine.reporting.FileReporter;
import com.arthanzel.theriverengine.reporting.RiverReporter;
import com.arthanzel.theriverengine.common.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.sim.RiverRunner;
import com.arthanzel.theriverengine.sim.RiverSystem;
import javafx.application.Application;
import javafx.stage.Stage;
import com.arthanzel.theriverengine.sim.influence.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
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
//        main.show();
//        Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // UI thread
//
//        runner.start();
    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: Get runner/model instance from Spring?
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) throws ParseException {
        // Set min priority on the MONITORING/UI thread.
        // The simulation runs in a different thread entirely.
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        CommandLine cmd = new DefaultParser().parse(options(), args);

        RiverRunner runner = setupSimulation();

        if (cmd.hasOption("x")) {
            // Run in headless mode without spinning up a JavaFX thread
            runner.setEnabled(true);
            runner.start();

            // Program does not terminate here as long as RiverRunner spins.
            // TODO: Require a finite end time
        }
        else {
            // Launch JavaFX
            Main.launch(args); // Blocking
        }
    }

    private static RiverRunner setupSimulation() {
        try {
            RiverNetwork network = RiverNetwork.fromResource("/graphs/binarytree-3.ini");
            RiverSystem system = new RiverSystem(network, 100);
            RiverRunner runner = new RiverRunner(system);
            return runner;
        }
        catch (IOException e) {
            // Error setting up the simulation.
            // Can't really recover from this...
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Generates a structure defining command-line options.
     */
    private static Options options() {
        Options options = new Options();
        options.addOption("x", "headless", false, "Run in headless mode without launching a JavaFX application");
        options.addOption("e", "end", true, "If headless, specify and end time in seconds (s, default), days (d), or years (y).");
        options.addOption("o", "out", true, "Output directory. Defaults to the current directory if missing or invalid.");
        return options;
    }
}
