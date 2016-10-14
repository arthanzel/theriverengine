package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.sim.RiverSystem;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverViewController {
    @FXML private RiverRenderer riverRenderer;

    private RiverSystem system;

    @SuppressWarnings("unused")
    public void initialize() {
        AnimationTimer anim = new AnimationTimer() {
            double skip = 0;

            @Override
            public void handle(long now) {
                skip++;
                if (skip == 3) {
                    skip = 0;
                    riverRenderer.update(system);
                }
            }
        };
        anim.start();
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
