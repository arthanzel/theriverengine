package com.arthanzel.theriverengine.gui;

import com.arthanzel.theriverengine.common.rivergen.RiverArc;
import com.arthanzel.theriverengine.common.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.common.rivergen.RiverNode;
import com.arthanzel.theriverengine.common.util.Benchmarks;
import com.arthanzel.theriverengine.gui.util.UIUtils;
import com.google.gson.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverRenderer extends Pane {
    private JsonObject root;
    private GraphicsContext g;
    private RenderOptions options;

    // Network cache
    private Map<String, RiverNode> nodes = new HashMap<>();
    private Map<String, RiverArc> arcs = new HashMap<>();

    public RiverRenderer() {
        Canvas canvas = new Canvas();
        this.getChildren().add(canvas);
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        g = canvas.getGraphicsContext2D();

        // Set up a clipping mask so JavaFX doesn't draw out of bounds
        UIUtils.enableClip(this);

        this.initUIEvents();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        }.start();
    }

    public void update(String json) {
        synchronized (this) {
            JsonParser p = new JsonParser();
            long n = System.nanoTime();
            Benchmarks.print("parsing", () -> root = p.parse(json).getAsJsonObject());

//            if (root.has("network")) {
//                try {
//                    updateNetworkCache(root.getAsJsonObject("network"));
//                } catch (IOException e) {
//                    System.err.println("Can't read network input!");
//                }
//            }
        }
    }

    // region Drawing

    private void draw() {
        if (root == null) {
            g.clearRect(0, 0, this.getWidth(), this.getHeight());
            g.fillText("Waiting for data...", 10, 20);
            return;
        }

        synchronized (this) {
            g.save();
            g.setTransform(new Affine());
            g.clearRect(0, 0, this.getWidth(), this.getHeight());
            drawScreenSpaceElements();
            g.restore();

            if (options.isRenderingNetwork()) drawNetwork();
            if (options.isRenderingAgents()) drawAgents();
        }
    }

    private void drawAgents() {
        for (JsonElement j : root.getAsJsonArray("agents")) {
            JsonObject agent = j.getAsJsonObject();
            String arc = agent.getAsJsonObject("location").get("arc").getAsString();
            double pos = agent.getAsJsonObject("location").get("position").getAsDouble();
            Point2D point = arcs.get(arc).getPoint(pos);
            double size = 5 / scale;
            // Draw agents in black if an environment is selected.
//            if (getRenderableEnvironment() == null) {
//                gfx.setFill((Color) a.getAttributes().get("color"));
//            }
            g.fillOval(point.getX() - size / 2, point.getY() - size / 2, size, size);
        }
    }

    private void drawNetwork() {
        for (RiverArc arc : arcs.values()) {
            RiverNode origin = arc.getUpstreamNode();
            RiverNode dest = arc.getDownstreamNode();
            g.setLineCap(StrokeLineCap.ROUND);
            g.setLineWidth(1.2 / this.scale);
            g.setStroke(Color.DARKBLUE);
            g.strokeLine(origin.getPosition().getX(),
                    origin.getPosition().getY(),
                    dest.getPosition().getX(),
                    dest.getPosition().getY());
        }
    }

    private void drawScreenSpaceElements() {
        final double w = this.getWidth();
        final double h = this.getHeight();

        // Draw the world coordinates under the mouse
        g.setFont(new Font(12));
        g.setFill(Color.BLACK);
        g.setTextBaseline(VPos.TOP);
        g.fillText(String.format("(%.2f, %.2f)", this.worldMouseX, this.worldMouseY),
                R.PADDING_X,
                R.PADDING_Y);

        // Draw the scale bar
        double lineSize = 100;
        g.setLineCap(StrokeLineCap.SQUARE);
        g.setStroke(Color.BLACK);
        g.setTextBaseline(VPos.BOTTOM);
        g.setLineWidth(4);
        g.strokeLine(w - R.PADDING_X - lineSize,
                h - R.PADDING_Y - 20,
                w - R.PADDING_X,
                h - R.PADDING_Y - 20);
        g.fillText(String.format("%.2f m", 100 / scale),
                w - R.PADDING_X - lineSize,
                h - R.PADDING_Y);
//
//
//        // Draw the legend bar
//        if (renderableEnvironment.get() != null) {
//            // Stop labels
//            final double barWidth = w / 2 - R.PADDING_X;
//            gfx.setLineWidth(1);
//            gfx.setStroke(Color.BLACK);
//            for (int i = 0; i < 5; i++) {
//                final double f = i / 4.0;
//                final double x = R.PADDING_X + f * barWidth;
//                final double min = options.getLegendMin();
//                final double max = options.getLegendMax();
//                gfx.strokeLine(x, h - R.PADDING_Y - 20, x, h - R.PADDING_Y - 14);
//                String ltgtIndicator = "";
//                if (i == 0) {
//                    ltgtIndicator = "<";
//                }
//                else if (i == 4) {
//                    ltgtIndicator = ">";
//                }
//                gfx.fillText(
//                        String.format("%s%.2f", ltgtIndicator, FishMath.lerp(min, max, f)),
//                        R.PADDING_X + f * barWidth,
//                        h - R.PADDING_Y);
//                gfx.setTextAlign(TextAlignment.CENTER);
//            }
//
//            // Color bar
//            final Paint GRADIENT = new LinearGradient(0, 0, 1 / MAX_HUE * 360, 0, true, CycleMethod.NO_CYCLE, UIUtils.HSB_STOPS);
//            gfx.setStroke(GRADIENT);
//            gfx.setLineWidth(3);
//            gfx.strokeLine(R.PADDING_X,
//                    h - R.PADDING_Y - 20,
//                    w / 2,
//                    h - R.PADDING_Y - 20);
//        }
    }

    // endregion

    // region UI

    private double originalX, originalY;
    private double worldMouseX, worldMouseY;
    private double scale = 1; // For easy use by this class

    private void initUIEvents() {
        // Pan on drag
        this.setOnMousePressed(me -> {
            originalX = me.getX();
            originalY = me.getY();
        });
        this.setOnMouseDragged(me -> {
            double dx = me.getX() - originalX;
            double dy = me.getY() - originalY;
            Affine transform = g.getTransform(); // Returns a copy
            transform.prependTranslation(dx, dy);
            g.setTransform(transform);
            originalX = me.getX();
            originalY = me.getY();
        });

        // Update world coordinates
        this.setOnMouseMoved(me -> {
            Point2D worldMouse = toWorldSpace(g.getTransform(), me.getX(), me.getY());
            worldMouseX = worldMouse.getX();
            worldMouseY = worldMouse.getY();
        });

        // Zoom on scroll
        this.setOnScroll(se -> {
            Affine transform = g.getTransform(); // Returns a copy
            Point2D preTransformMouse = toWorldSpace(transform, se.getX(), se.getY());

            // Scale the canvas
            if (se.getDeltaY() > 0) {
                transform.appendScale(R.ZOOM_FACTOR, R.ZOOM_FACTOR);
                scale *= R.ZOOM_FACTOR;
            } else {
                transform.appendScale(1 / R.ZOOM_FACTOR, 1 / R.ZOOM_FACTOR);
                scale /= R.ZOOM_FACTOR;
            }

            Point2D postTransformMouse = new Point2D(0, 0);
            postTransformMouse = toWorldSpace(transform, se.getX(), se.getY());

            transform.appendTranslation(postTransformMouse.getX() - preTransformMouse.getX(),
                    postTransformMouse.getY() - preTransformMouse.getY());

            g.setTransform(transform);
        });
    }

    // endregion

    // region Utility Methods

    private Point2D toWorldSpace(Affine transform, double x, double y) {
        try {
            return transform.inverseTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            System.err.println("Non invertible on " + x + ", " + y);
            return new Point2D(0, 0);
        }
    }

    public void updateNetworkCache(RiverNetwork network) {
        for (RiverNode node : network.vertexSet()) {
            this.nodes.put(node.getName(), node);
        }
        for (RiverArc arc : network.edgeSet()) {
            this.arcs.put(arc.toString(), arc);
        }
    }

    // endregion

    // region Accessors

    public RenderOptions getOptions() {
        return options;
    }

    public void setOptions(RenderOptions options) {
        this.options = options;
    }

    // endregion



//
//
//    // endregion
//
//    // region Drawing Methods
//    // ======================
//
//    /**
//     * Draws markers that represent agents.
//     *
//     * @param agents Array of agents.
//     */
//    private void drawAgents(List<Agent> agents) {
//        gfx.setFill(Color.BLACK);
//        for (Agent a : agents) {
//            Point2D point = a.getLocation().getPoint();
//            double size = 5 / scale;
//
//            // Draw agents in black if an environment is selected.
//            if (getRenderableEnvironment() == null) {
//                gfx.setFill((Color) a.getAttributes().get("color"));
//            }
//
//            gfx.fillOval(point.getX() - size / 2, point.getY() - size / 2, size, size);
//        }
//    }
//
//    /**
//     * Draws extra information on the screen.
//     */
//    private void drawExtras(RiverSystem system) {
//        gfx.setFill(Color.BLACK);
//        gfx.setTextAlign(TextAlignment.CENTER);
//        gfx.setTextBaseline(VPos.BOTTOM);
//        for (RiverNode node : system.getNetwork().vertexSet()) {
//            Point2D p = node.getPosition();
//            gfx.fillText(node.getName(), p.getX(), p.getY() - 2);
//        }
//
//        gfx.setTextAlign(TextAlignment.LEFT);
//        gfx.setTextBaseline(VPos.CENTER);
//        for (Agent a : system.getAgents()) {
//            Point2D p = a.getLocation().getPoint();
//            gfx.fillText(String.format("%.2f", a.getAttributes().getDouble("energy")), p.getX() + 5, p.getY() + 5);
//        }
//    }
//
//    /**
//     * Draws a visualization of a river network.
//     *
//     * @param network A river network.
//     */
//    private void drawNetwork(RiverNetwork network) {
//        for (RiverArc arc : network.edgeSet()) {
//            RiverNode origin = arc.getUpstreamNode();
//            RiverNode dest = arc.getDownstreamNode();
//            gfx.setLineCap(StrokeLineCap.ROUND);
//            gfx.setLineWidth(1.2 / this.scale);
//            gfx.setStroke(Color.DARKBLUE);
//            gfx.strokeLine(origin.getPosition().getX(),
//                    origin.getPosition().getY(),
//                    dest.getPosition().getX(),
//                    dest.getPosition().getY());
//        }
//    }
//
//    /**
//     * Draws elements that are statically-positioned on the screen, and are not affected by the
//     * translate and scale transforms, such as the coordinate display and the scale bar.
//     */
//    private void drawScreenSpaceElements() {
//
//    }
//
//    private void drawEnvironment(RiverSystem system) {
//        Environment env = system.getEnvironments().get(renderableEnvironment.get());
//
//        gfx.setLineCap(StrokeLineCap.SQUARE);
//
//        for (RiverArc arc : system.getNetwork().edgeSet()) {
//            // Draw ticks on every data point if it is a discrete environment
////            if (scale > 4 && env instanceof DiscreteEnvironment) {
////                DiscreteEnvironment denv = (DiscreteEnvironment) env;
////                gfx.save();
////                gfx.setFill(Color.GRAY);
////                gfx.setLineWidth(1 / scale);
////                double tickDimension = 5 / scale;
////                DiscretePoint[] points = denv.getPoints(arc);
////                for (int i = 1; i < points.length - 1; i++) {
////                    Point2D p = arc.getPoint(points[i].getPosition());
////                    gfx.fillOval(p.getX() - tickDimension / 2, p.getY() - tickDimension / 2, tickDimension, tickDimension);
////                }
////                gfx.restore();
////            }
//
//            gfx.setLineWidth(2 / scale);
//
//            // Determine the number of drawing primitives per line
//            //int n = (int) Math.ceil(arc.length() * this.scale / INTERVAL_PX);
//            int n = (int) (arc.length() / options.getEnvTickInterval());
//
//            double lastHue = 0;
//            Point2D lastPoint = null;
//            for (int i = 0; i <= n; i++) {
//                // Position along the arc from 0 to 1
//                double f = 1.0 * i / n;
//
//                // Value of the environment at that point
//                double envValue = env.get(arc, f * arc.length());
//
//                // Fraction of the environment value along the current scale.
//                // 0 is the minimum value, 1 is the maximum.
//                double envF = (envValue - options.getLegendMin()) / (options.getLegendMax() - options.getLegendMin());
//
//                double pointHue = FishMath.clamp(envF, 0, 1) * MAX_HUE;
//                Point2D point = arc.getPointLerp(f);
//
//                // Construct a gradient line from the current point to the previous one
//                if (lastPoint != null) {
//                    double angle = point.angle(lastPoint);
//                    Paint gradient = new LinearGradient(
//                            point.getX(),
//                            point.getY(),
//                            lastPoint.getX(),
//                            lastPoint.getY(),
//                            false,
//                            CycleMethod.NO_CYCLE, // For some reason, lines produce small overhangs where line ends
//                                                  // round to the closest pixel, so REFLECT produces random
//                                                  // discolourations at the end of lines. NO_CYCLE preserves colour.
//                            UIUtils.hueGradient(pointHue, lastHue)
//                    );
//                    gfx.setStroke(gradient);
//                    gfx.strokeLine(point.getX(), point.getY(), lastPoint.getX(), lastPoint.getY());
//                }
//
//                lastHue = pointHue;
//                lastPoint = point;
//            }
//        }
//    }
//
//    // endregion
//
//    // endregion
//
//    // region Accessors
//    // ================
//
//    public RenderOptions getOptions() {
//        return options;
//    }
//
//    public String getRenderableEnvironment() {
//        return renderableEnvironment.get();
//    }
//
//    public StringProperty renderableEnvironmentProperty() {
//        return renderableEnvironment;
//    }
//
//    public void setRenderableEnvironment(String renderableEnvironment) {
//        this.renderableEnvironment.set(renderableEnvironment);
//    }
//
//    // endregion
}
