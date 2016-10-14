package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import com.arthanzel.theriverengine.sim.RiverSystem;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverRenderer extends Pane {
    private Canvas canvas;
    private GraphicsContext gfx;

    public RiverRenderer() {
        // Set up the canvas.
        // The canvas must be redrawn every time this pane is resized.
        this.canvas = new Canvas();
        this.getChildren().add(canvas);
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        this.widthProperty().addListener(ev -> {
            // TODO: Don't redraw the canvas, but show a message until the next update.
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
    }

    public void update(RiverSystem system) {
        gfx.save();
        gfx.setTransform(new Affine());
        gfx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gfx.restore();

        if (system == null) {
            System.out.println("WARN: RiverSystem is null on view update!");
            return;
        }

        drawNetwork(system.getNetwork());
    }

    // region UI
    // =========

    private double originalX, originalY;

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

        // Zoom on scroll
        this.setOnScroll(se -> {
            Affine transform = gfx.getTransform(); // Returns a copy
            Point2D preTransformMouse = toWorldSpace(transform, se.getX(), se.getY());

            // Scale the canvas
            if (se.getDeltaY() > 0) {
                transform.appendScale(UIConstants.ZOOM_FACTOR, UIConstants.ZOOM_FACTOR);
            } else {
                transform.appendScale(1 / UIConstants.ZOOM_FACTOR, 1 / UIConstants.ZOOM_FACTOR);
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

    private void drawNetwork(RiverNetwork network) {
        for (RiverArc arc : network.edgeSet()) {
            RiverNode origin = arc.getUpstreamNode();
            RiverNode dest = arc.getDownstreamNode();
            gfx.setLineCap(StrokeLineCap.ROUND);
            gfx.strokeLine(origin.getPosition().getX(),
                    origin.getPosition().getY(),
                    dest.getPosition().getX(),
                    dest.getPosition().getY());
        }
    }

//    private void drawScaleBar() {
//        scaleGroup.getChildren().clear();
//        Line line = new Line(0, 0, 100, 0);
//        line.setStrokeWidth(4);
//        line.setStrokeLineCap(StrokeLineCap.SQUARE);
//        scaleGroup.getChildren().add(line);
//
//        scaleGroup.setTranslateX(20);
//        System.out.println(this.getHeight());
//        scaleGroup.setTranslateY(this.getHeight() - 140);
//    }

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
}
