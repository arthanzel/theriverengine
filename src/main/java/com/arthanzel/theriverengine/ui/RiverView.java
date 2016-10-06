package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNetwork;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverView extends Stage {
    RiverNetwork network;
    Group root;

    public RiverView(RiverNetwork network) {
        this.network = network;
        this.root = new Group();

        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setWidth(UIConstants.INITIAL_WIDTH);
        this.setHeight(UIConstants.INITIAL_HEIGHT);

        scene.setOnKeyPressed(ke -> {
            switch (ke.getCode()) {
                case RIGHT:
                    root.setTranslateX(root.getTranslateX() - UIConstants.MOVE_DISTANCE);
                    break;
                case LEFT:
                    root.setTranslateX(root.getTranslateX() + UIConstants.MOVE_DISTANCE);
                    break;
                case UP:
                    root.setTranslateY(root.getTranslateY() + UIConstants.MOVE_DISTANCE);
                    break;
                case DOWN:
                    root.setTranslateY(root.getTranslateY() - UIConstants.MOVE_DISTANCE);
                    break;
            }
        });

        initDragging(scene, root);
        drawNetwork();
    }

    private void drawNetwork() {
        for (RiverArc arc : network.edgeSet()) {
            RiverNode origin = arc.getUpstreamNode();
            RiverNode dest = arc.getDownstreamNode();
            Line l = new Line(origin.getPosition().getX(),
                    origin.getPosition().getY(),
                    dest.getPosition().getX(),
                    dest.getPosition().getY());
            root.getChildren().add(l);
        }
    }

    private double originalX = 0;
    private double originalY = 0;
    private double rootX = 0;
    private double rootY = 0;
    private void initDragging(Scene scene, Group group) {
        scene.setOnMousePressed(me -> {
            originalX = me.getX();
            originalY = me.getY();
            rootX = group.getTranslateX();
            rootY = group.getTranslateY();
        });
        scene.setOnMouseDragged(me -> {
            double dx = me.getX() - originalX;
            double dy = me.getY() - originalY;
            group.setTranslateX(rootX + dx);
            group.setTranslateY(rootY + dy);
        });
        scene.setOnScroll(se -> {
            if (se.getDeltaY() > 0) {
                group.setScaleX(group.getScaleX() * UIConstants.ZOOM_FACTOR);
                group.setScaleY(group.getScaleY() * UIConstants.ZOOM_FACTOR);
            }
            else {
                group.setScaleX(group.getScaleX() / UIConstants.ZOOM_FACTOR);
                group.setScaleY(group.getScaleY() / UIConstants.ZOOM_FACTOR);
            }
        });
    }
}
