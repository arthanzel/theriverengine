package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import com.arthanzel.theriverengine.sim.RiverSystem;
import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverRenderer extends Region {
    Group root;
    Group scaleGroup;

    public RiverRenderer() {
        Rectangle clip = new Rectangle(this.getWidth(), this.getHeight());
        clip.widthProperty().bind(this.widthProperty());
        clip.heightProperty().bind(this.heightProperty());
        this.setClip(clip);
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

    // endregion
}
