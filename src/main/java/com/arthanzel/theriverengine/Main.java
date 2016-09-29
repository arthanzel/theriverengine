package com.arthanzel.theriverengine;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.DijkstraShortestPath;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

/**
 * Entry point for The River Engine.
 *
 * @author Martin
 */
public class Main extends Application {
    @Override
    public void start(Stage main) {

    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
