package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import com.arthanzel.theriverengine.sim.environment.DiscreteEnvironment;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import com.arthanzel.theriverengine.sim.environment.Environment;
import com.arthanzel.theriverengine.util.FishMath;
import com.arthanzel.theriverengine.util.FrameCounter;
import com.arthanzel.theriverengine.util.PaintUtils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverRenderer extends Pane {
    // Constants
    private static final double MAX_HUE = 360 * 0.85;

    private Canvas canvas;
    private GraphicsContext gfx;
    private FrameCounter counter = new FrameCounter("RiverRenderer", 1000);
    private RenderOptions options = new RenderOptions();

    // Properties
    private DoubleProperty fps = new SimpleDoubleProperty(0);
    private ObjectProperty<Environment> renderableEnvironment = new SimpleObjectProperty<>();

    public RiverRenderer() {
        // Set up the canvas.
        // The canvas must be redrawn every time this pane is resized.
        this.canvas = new Canvas();
        this.getChildren().add(canvas);
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        this.widthProperty().addListener(ev -> {
            // TODO: clone the model
            update(null);
        });
        this.heightProperty().addListener(ev -> {
            update(null);
        });
        gfx = this.canvas.getGraphicsContext2D();

        // Set up a clipping mask so JavaFX doesn't draw out of bounds
        Rectangle clip = new Rectangle(this.getWidth(), this.getHeight());
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        this.setClip(clip);

        initUIEvents();

        // Set up the frame counter
        counter.setOnReport(fps -> {
            this.fps.set(fps);
        });
        counter.start();
    }

    public void update(RiverSystem system) {
        if (system == null) {
            System.out.println("WARN: RiverSystem is null on view update!");
            return;
        }

        // TODO: Don't synchronize here, but clone
        synchronized (system) {
            counter.increment();

            gfx.save();
            gfx.setTransform(new Affine());
            gfx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawScreenSpaceElements();
            gfx.restore();

            if (options.isRenderingNetwork()) drawNetwork(system.getNetwork());
            if (this.renderableEnvironment.get() != null) drawEnvironment(system);
            if (options.isRenderingAgents()) drawAgents(system.getAgents());
        }
    }

    // region UI
    // =========

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
            Affine transform = gfx.getTransform(); // Returns a copy
            transform.prependTranslation(dx, dy);
            gfx.setTransform(transform);
            originalX = me.getX();
            originalY = me.getY();
        });

        // Update world coordinates
        this.setOnMouseMoved(me -> {
            Point2D worldMouse = toWorldSpace(gfx.getTransform(), me.getX(), me.getY());
            worldMouseX = worldMouse.getX();
            worldMouseY = worldMouse.getY();
        });

        // Zoom on scroll
        this.setOnScroll(se -> {
            Affine transform = gfx.getTransform(); // Returns a copy
            Point2D preTransformMouse = toWorldSpace(transform, se.getX(), se.getY());

            // Scale the canvas
            if (se.getDeltaY() > 0) {
                transform.appendScale(UIConstants.ZOOM_FACTOR, UIConstants.ZOOM_FACTOR);
                scale *= UIConstants.ZOOM_FACTOR;
            } else {
                transform.appendScale(1 / UIConstants.ZOOM_FACTOR, 1 / UIConstants.ZOOM_FACTOR);
                scale /= UIConstants.ZOOM_FACTOR;
            }

            Point2D postTransformMouse = new Point2D(0, 0);
            postTransformMouse = toWorldSpace(transform, se.getX(), se.getY());

            transform.appendTranslation(postTransformMouse.getX() - preTransformMouse.getX(),
                    postTransformMouse.getY() - preTransformMouse.getY());

            gfx.setTransform(transform);
        });
    }

    // endregion

    // region Drawing Methods
    // ======================

    /**
     * Draws markers that represent agents.
     *
     * @param agents Array of agents.
     */
    private void drawAgents(Agent[] agents) {
        gfx.setFill(Color.BLACK);
        for (Agent a : agents) {
            Point2D point = a.getLocation().getPoint();
            double size = 5 / scale;
            gfx.fillOval(point.getX() - size / 2, point.getY() - size / 2, size, size);
        }
    }

    /**
     * Draws a visualization of a river network.
     *
     * @param network A river network.
     */
    private void drawNetwork(RiverNetwork network) {
        for (RiverArc arc : network.edgeSet()) {
            RiverNode origin = arc.getUpstreamNode();
            RiverNode dest = arc.getDownstreamNode();
            gfx.setLineCap(StrokeLineCap.ROUND);
            gfx.setLineWidth(1.2 / this.scale);
            gfx.setStroke(Color.MEDIUMBLUE);
            gfx.strokeLine(origin.getPosition().getX(),
                    origin.getPosition().getY(),
                    dest.getPosition().getX(),
                    dest.getPosition().getY());
        }
    }

    /**
     * Draws elements that are statically-positioned on the screen, and are not affected by the
     * translate and scale transforms, such as the coordinate display and the scale bar.
     */
    private void drawScreenSpaceElements() {
        final double w = this.getWidth();
        final double h = this.getHeight();

        // Draw the world coordinates under the mouse
        gfx.setFont(new Font(12));
        gfx.setFill(Color.BLACK);
        gfx.setTextBaseline(VPos.TOP);
        gfx.fillText(String.format("(%.2f, %.2f)", this.worldMouseX, this.worldMouseY),
                UIConstants.PADDING_X,
                UIConstants.PADDING_Y);

        // Draw the scale bar
        double lineSize = 100;
        gfx.setLineCap(StrokeLineCap.SQUARE);
        gfx.setStroke(Color.BLACK);
        gfx.setTextBaseline(VPos.BOTTOM);
        gfx.setLineWidth(4);
        gfx.strokeLine(w - UIConstants.PADDING_X - lineSize,
                h - UIConstants.PADDING_Y - 20,
                w - UIConstants.PADDING_X,
                h - UIConstants.PADDING_Y - 20);
        gfx.fillText(String.format("%.2f m", 100 / scale),
                w - UIConstants.PADDING_X - lineSize,
                h - UIConstants.PADDING_Y);


        // Draw the legend bar
        if (renderableEnvironment.get() != null) {
            // Stop labels
            final double barWidth = w / 2 - UIConstants.PADDING_X;
            gfx.setLineWidth(1);
            gfx.setStroke(Color.BLACK);
            for (int i = 0; i < 5; i++) {
                final double f = i / 4.0;
                final double x = UIConstants.PADDING_X + f * barWidth;
                final double min = options.getLegendMin();
                final double max = options.getLegendMax();
                gfx.strokeLine(x, h - UIConstants.PADDING_Y - 20, x, h - UIConstants.PADDING_Y - 14);
                String ltgtIndicator = "";
                if (i == 0) {
                    ltgtIndicator = "<";
                }
                else if (i == 4) {
                    ltgtIndicator = ">";
                }
                gfx.fillText(
                        String.format("%s%.2f", ltgtIndicator, FishMath.lerp(min, max, f)),
                        UIConstants.PADDING_X + f * barWidth,
                        h - UIConstants.PADDING_Y);
                gfx.setTextAlign(TextAlignment.CENTER);
            }

            // Color bar
            final Paint GRADIENT = new LinearGradient(0, 0, 1 / MAX_HUE * 360, 0, true, CycleMethod.NO_CYCLE, PaintUtils.HSB_STOPS);
            gfx.setStroke(GRADIENT);
            gfx.setLineWidth(3);
            gfx.strokeLine(UIConstants.PADDING_X,
                    h - UIConstants.PADDING_Y - 20,
                    w / 2,
                    h - UIConstants.PADDING_Y - 20);
        }
    }

    private void drawEnvironment(RiverSystem system) {
        double INTERVAL_PX = 10;

        //TODO: Uniform intervals
        Environment env = this.renderableEnvironment.get();
        gfx.setLineWidth(2 / scale);
        gfx.setLineCap(StrokeLineCap.SQUARE);

        for (RiverArc arc : system.getNetwork().edgeSet()) {
            // Determine the number of drawing primitives per line
            //int n = (int) Math.ceil(arc.length() * this.scale / INTERVAL_PX);
            int n = (int) (arc.length() / 1);

            for (double i = 0; i < 1; i += 1.0 / n) {
                double envValue = env.get(arc, i * arc.length());
                double fraction = (envValue - options.getLegendMin()) / (options.getLegendMax() - options.getLegendMin());
                gfx.setStroke(Color.hsb(FishMath.clamp(fraction, 0, 1) * MAX_HUE, 1, 1));
                Point2D point = arc.getPointLerp(i);
                Point2D point2 = arc.getPointLerp(Math.min(1, i + 1.0 / n));
                gfx.strokeLine(point.getX(), point.getY(), point2.getX(), point2.getY());
            }
        }
    }

    // endregion

    // region Utility Methods
    // ======================

    private Point2D toScreenSpace(Affine transform, double x, double y) {
        return transform.transform(x, y);
    }

    private Point2D toWorldSpace(Affine transform, double x, double y) {
        try {
            return transform.inverseTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            System.out.println("Non invertible on " + x + ", " + y);
            return new Point2D(0, 0);
        }
    }

    // endregion

    // region Accessors
    // ================

    public double getFps() {
        return fps.get();
    }

    public DoubleProperty fpsProperty() {
        return fps;
    }

    public RenderOptions getOptions() {
        return options;
    }

    public Environment getRenderableEnvironment() {
        return renderableEnvironment.get();
    }

    public ObjectProperty<Environment> renderableEnvironmentProperty() {
        return renderableEnvironment;
    }

    public void setRenderableEnvironment(Environment renderableEnvironment) {
        this.renderableEnvironment.set(renderableEnvironment);
    }

    // endregion
}
