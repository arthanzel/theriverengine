package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import com.arthanzel.theriverengine.sim.RiverSystem;
import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverRenderer extends Region {
    private Group root;

    public RiverRenderer() {
        Rectangle clip = new Rectangle(this.getWidth(), this.getHeight());
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        this.setClip(clip);
        initUIEvents();
    }

    /**
     * Initialize this RiverRenderer to a RiverNetwork and performs one-time drawing operations, such as drawing the
     * network.
     */
    public void initialize(RiverSystem system) {
        System.out.println("Initialized renderer");

        root = new Group();
        this.getChildren().clear();
        this.getChildren().add(root);

        drawNetwork(system.getNetwork());
    }

    public void update(RiverSystem system) {

    }

    // region UI
    // =========

    private double originalX, originalY, rootX, rootY;
    private void initUIEvents() {
        // Pan on drag
        this.setOnMousePressed(me -> {
            originalX = me.getX();
            originalY = me.getY();
            rootX = root.getTranslateX();
            rootY = root.getTranslateY();
        });
        this.setOnMouseDragged(me -> {
            double dx = me.getX() - originalX;
            double dy = me.getY() - originalY;
            root.setTranslateX(rootX + dx);
            root.setTranslateY(rootY + dy);
        });

        // Zoom on scroll
        this.setOnScroll(se -> {
            if (se.getDeltaY() > 0) {
                root.setScaleX(root.getScaleX() * UIConstants.ZOOM_FACTOR);
                root.setScaleY(root.getScaleY() * UIConstants.ZOOM_FACTOR);
            } else {
                root.setScaleX(root.getScaleX() / UIConstants.ZOOM_FACTOR);
                root.setScaleY(root.getScaleY() / UIConstants.ZOOM_FACTOR);
            }
        });
    }

    // endregion

    // region Drawing Methods
    // ======================

    private void drawNetwork(RiverNetwork network) {
        for (RiverArc arc : network.edgeSet()) {
            RiverNode origin = arc.getUpstreamNode();
            RiverNode dest = arc.getDownstreamNode();
            Line l = new Line(origin.getPosition().getX(),
                    origin.getPosition().getY(),
                    dest.getPosition().getX(),
                    dest.getPosition().getY());
            this.root.getChildren().add(l);
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
}
