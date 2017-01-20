package com.arthanzel.theriverengine.sim.environment;

import com.arthanzel.theriverengine.common.rivergen.RiverArc;
import com.arthanzel.theriverengine.common.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.common.util.Graphs;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

/**
 * Created by martin on 2016-11-29.
 */
public class MatrixEnvironment implements Environment {
    public static final double DEFAULT_RESOLUTION = 5;

    private double[][] values;
    private Rectangle2D dimensions;
    private double resolution;

    private MatrixEnvironment() {}

    public MatrixEnvironment(RiverNetwork network) {
        this(network, DEFAULT_RESOLUTION);
    }

    public MatrixEnvironment(RiverNetwork network, double resolution) {
        this.dimensions = Graphs.dimensions(network);
        this.resolution = resolution;
        final int width = (int) (dimensions.getWidth() / resolution);
        final int height = (int) (dimensions.getHeight() / resolution);
        this.values = new double[width][height];

        // Init to random values
        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[x].length; y++) {
                values[x][y] = Math.random();
            }
        }
    }

    @Override
    public double get(RiverArc arc, double position) {
        Point2D coord = arc.getPoint(position);
        double x = (coord.getX() - dimensions.getMinX()) / resolution;
        double y = (coord.getY() - dimensions.getMinY()) / resolution;
        try {
            return values[(int) x][(int) y];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public MatrixEnvironment clone() {
        MatrixEnvironment env = new MatrixEnvironment();
        env.values = new double[this.values.length][this.values[0].length];
        env.dimensions = this.dimensions;
        env.resolution = resolution;
        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[x].length; y++) {
                env.values[x][y] = this.values[x][y];
            }
        }
        return env;
    }
}
