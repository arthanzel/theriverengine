package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.reporting.FileReporter;
import com.arthanzel.theriverengine.common.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.sim.RiverRunner;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.environment.DiscreteEnvironment;
import com.arthanzel.theriverengine.util.prefs.RiverPrefs;
import javafx.application.Application;
import com.arthanzel.theriverengine.sim.influence.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Entry point for The River Engine.
 *
 * @author Martin
 */
public class Main {
    /**
     * Global simulation runner.
     * Globals are evil, but they are also the easiest way to pass a runner
     * to the admin application, since JavaFX has a weird-as-fudge launch
     * process.
     */
    public static RiverRunner RUNNER;

    private static CommandLine cmd;

    public static void main(String[] args) throws ParseException {
        // Set min priority on the MONITORING/UI thread.
        // The simulation runs in a different thread entirely.
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        cmd = new DefaultParser().parse(options(), args);

        RiverRunner runner = setupSimulation();
        RUNNER = runner;

        // -x starts in headless mode, without spawning a JavaFX application and
        // showing the admin UI. Use on servers running scheduled or batch
        // simulations.
        if (cmd.hasOption("x")) {
            // Run in headless mode without spinning up a JavaFX thread
            runner.setEnabled(true);
            runner.start();

            // Program does not terminate here as long as RiverRunner spins.
            // TODO: Require a finite end time
        }
        else {
            runner.start();

            // Launch JavaFX
            try {
                Application.launch(MainApplication.class, args); // Blocking
            }
            catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error while launching the JavaFX application.");
                System.err.println("Try using the -x flag to launch in headless mode.");
            }
        }
    }

    /**
     * Constructs a RiverRunner fully configured to run a simulation.
     */
    private static RiverRunner setupSimulation() {
        try {
            RiverNetwork network = RiverNetwork.fromResource("/graphs/binarytree-3.ini");
            RiverSystem system = new RiverSystem(network, 3);
            RiverRunner runner = new RiverRunner(system);
            runner.setEnabled(false);

            // Environments
            system.getEnvironments().put("nutrients", new DiscreteEnvironment(network));

            // Influences
            runner.getInfluences().add(new RandomMovement());
            runner.getInfluences().add(new FlowMovement());
            runner.getInfluences().add(new VelocityApplier());
            runner.getInfluences().add(new NutrientDynamicsLog());
            runner.getInfluences().add(new FeedingInfluence());
            runner.getInfluences().add(new DeathDynamics());
            runner.getInfluences().add(new ReproductionDynamics(system));

            // Prefs
            RiverPrefs prefs = new RiverPrefs("/prefs/defaultPrefs.ini");
            prefs.set(runner.getInfluences());

            // Reporters
            String uuid = UUID.randomUUID().toString().substring(0, 4);
            String fileName = String.format("%s-%s.json",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH.mm.ss")),
                    uuid);
            runner.getReporter().getConsumers().add(new FileReporter(new File("data/" + fileName), system));

            return runner;
        }
        catch (Exception e) {
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
