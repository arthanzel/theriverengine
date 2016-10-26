package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.sim.influence.Influence;
import com.arthanzel.theriverengine.sim.RiverRunner;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.ui.controls.BeanEditPane;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverViewController {
    @FXML private Accordion optionsAccordion;
    @FXML private RiverRenderer riverRenderer;
    @FXML private Label fpsLabel;

    private RiverSystem system;

    @SuppressWarnings("unused")
    public void initialize() {
        AnimationTimer anim = new AnimationTimer() {
            double skip = 0;

            @Override
            public void handle(long now) {
                riverRenderer.update(system);
//                skip++;
//                if (skip == 1) {
//                    skip = 0;
//                    riverRenderer.update(system);
//                }
            }
        };
        anim.start();

        riverRenderer.fpsProperty().addListener((observable, oldValue, newValue) -> {
            // FIXME: This event is coming from a different thread, which JavaFX does not like.
            //fpsLabel.setText(newValue + " FPS");
        });
    }

    public void initialize(RiverSystem system, RiverRunner runner) {
        this.system = system;

        optionsAccordion.getPanes().add(new BeanEditPane("Render Options", riverRenderer.getOptions()));
        for (Influence i : runner.getInfluences()) {
            optionsAccordion.getPanes().add(new BeanEditPane(i));
        }
    }

    // ====== Accessors ======

    public RiverRenderer getRiverRenderer() {
        return riverRenderer;
    }

    public void setRiverRenderer(RiverRenderer riverRenderer) {
        this.riverRenderer = riverRenderer;
    }

    public RiverSystem getSystem() {
        return system;
    }

    public void setSystem(RiverSystem system) {
        this.system = system;
    }
}
