package com.arthanzel.theriverengine.gui;

import com.arthanzel.theriverengine.common.rivergen.RiverArc;
import com.arthanzel.theriverengine.common.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.common.rivergen.RiverNode;
import com.arthanzel.theriverengine.common.util.Benchmarks;
import com.arthanzel.theriverengine.common.util.FishMath;
import com.arthanzel.theriverengine.gui.util.UIUtils;
import com.google.gson.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
    private String selectedEnvironment = "";

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

    public void update(JsonObject json) {
        this.root = json;
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
            drawEnvironment();
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

    private void drawEnvironment() {
        if (!root.getAsJsonObject("environments").has(selectedEnvironment)) {
            return;
        }

        g.save();
        g.setLineWidth(2 / scale);
        g.setLineCap(StrokeLineCap.SQUARE);

        JsonObject jsonEnvironment = root.getAsJsonObject("environments").getAsJsonObject(selectedEnvironment).getAsJsonObject("arcs");
        for (RiverArc arc : arcs.values()) {
            JsonArray vals = jsonEnvironment.getAsJsonArray(arc.toString());

            double lastHue = 0;
            Point2D lastPoint = null;
            for (int i = 0; i < vals.size(); i++) {
                double f = 1.0 * i / (vals.size() - 1); // Position of the point on the arc
                double v = vals.get(i).getAsDouble(); // Value at that point
                double hue = FishMath.clamp(FishMath.lerp(options.getLegendMin(), options.getLegendMax(), v), 0, 1) * R.MAX_HUE;
                Point2D p = arc.getPointLerp(f);

                // Construct a gradient line from the current point to the previous one
                if (lastPoint != null) {
                    Paint gradient = new LinearGradient(
                            p.getX(),
                            p.getY(),
                            lastPoint.getX(),
                            lastPoint.getY(),
                            false,
                            CycleMethod.NO_CYCLE,
                            UIUtils.hueGradient(hue, lastHue)
                    );
                    g.setStroke(gradient);
                    g.strokeLine(p.getX(), p.getY(), lastPoint.getX(), lastPoint.getY());
                }

                lastHue = hue;
                lastPoint = p;
            }
        }

        g.restore();
    }

    private void drawNetwork() {
        g.save();
        g.setLineCap(StrokeLineCap.ROUND);
        g.setLineWidth(1.2 / this.scale);
        g.setStroke(Color.DARKBLUE);

        for (RiverArc arc : arcs.values()) {
            RiverNode origin = arc.getUpstreamNode();
            RiverNode dest = arc.getDownstreamNode();
            g.strokeLine(origin.getPosition().getX(),
                    origin.getPosition().getY(),
                    dest.getPosition().getX(),
                    dest.getPosition().getY());
        }

        g.restore();
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

        if (/*renderableEnvironment.get() != null*/true) {
            // Stop labels
            final double barWidth = w / 2 - R.PADDING_X;
            g.setLineWidth(1);
            g.setStroke(Color.BLACK);
            for (int i = 0; i < 5; i++) {
                final double f = i / 4.0;
                final double x = R.PADDING_X + f * barWidth;
                final double min = options.getLegendMin();
                final double max = options.getLegendMax();
                g.strokeLine(x, h - R.PADDING_Y - 20, x, h - R.PADDING_Y - 14);
                String ltgtIndicator = "";
                if (i == 0) {
                    ltgtIndicator = "<";
                }
                else if (i == 4) {
                    ltgtIndicator = ">";
                }
                g.fillText(
                        String.format("%s%.2f", ltgtIndicator, FishMath.lerp(min, max, f)),
                        R.PADDING_X + f * barWidth,
                        h - R.PADDING_Y);
                g.setTextAlign(TextAlignment.CENTER);
            }

            // Color bar
            final Paint GRADIENT = new LinearGradient(0, 0, 1 / R.MAX_HUE * 360, 0, true, CycleMethod.NO_CYCLE, UIUtils.HSB_STOPS);
            g.setStroke(GRADIENT);
            g.setLineWidth(3);
            g.strokeLine(R.PADDING_X,
                    h - R.PADDING_Y - 20,
                    w / 2,
                    h - R.PADDING_Y - 20);
        }
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

    public String getSelectedEnvironment() {
        return selectedEnvironment;
    }

    public void setSelectedEnvironment(String selectedEnvironment) {
        this.selectedEnvironment = selectedEnvironment;
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
