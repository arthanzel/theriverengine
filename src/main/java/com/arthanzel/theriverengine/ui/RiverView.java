package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.rivergen.RiverArc;
import com.arthanzel.theriverengine.rivergen.RiverNode;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.agent.Agent;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverView extends Stage {
    RiverSystem system;
    Group root;

    // Caches
    Map<Agent, Circle> agentMarkers = new HashMap<>();

    public RiverView(RiverSystem system) {
        this.system = system;
        this.root = new Group();

        // Allow a small margin on the top and left.
        root.setTranslateX(UIConstants.INITIAL_MARGIN);
        root.setTranslateY(UIConstants.INITIAL_MARGIN);

        Scene scene = new Scene(root);
        this.setScene(scene);
        this.setWidth(UIConstants.INITIAL_WIDTH);
        this.setHeight(UIConstants.INITIAL_HEIGHT);

        initDragging(scene, root);
        initAgentMarkers(root);
        drawNetwork();
        update();
    }

    public void update() {
        agentMarkers.forEach((a, c) -> {
            Point2D pos = a.getLocation().getPoint();
            c.setCenterX(pos.getX());
            c.setCenterY(pos.getY());
        });
    }

    private void drawNetwork() {
        for (RiverArc arc : system.getNetwork().edgeSet()) {
            RiverNode origin = arc.getUpstreamNode();
            RiverNode dest = arc.getDownstreamNode();
            Line l = new Line(origin.getPosition().getX(),
                    origin.getPosition().getY(),
                    dest.getPosition().getX(),
                    dest.getPosition().getY());
            root.getChildren().add(l);
        }
    }

    private void initAgentMarkers(Group root) {
        for (Agent a : system.getAgents()) {
            Circle c = new Circle(3, Color.BLUE);
            root.getChildren().add(c);
            agentMarkers.put(a, c);
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
            } else {
                group.setScaleX(group.getScaleX() / UIConstants.ZOOM_FACTOR);
                group.setScaleY(group.getScaleY() / UIConstants.ZOOM_FACTOR);
            }
        });
    }
}
