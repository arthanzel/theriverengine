package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.adminui.AdminUI;
import com.arthanzel.theriverengine.sim.RiverRunner;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by martin on 2017-02-11.
 */
public class MainApplication extends Application {
    private RiverRunner runner;

    @Override
    public void start(Stage primaryStage) throws Exception {
        runner = Main.RUNNER;
        AdminUI ui = new AdminUI(runner);

        // There are runtime exit hooks elsewhere in the code...
        ui.setOnCloseRequest(event -> System.exit(0));

        ui.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
