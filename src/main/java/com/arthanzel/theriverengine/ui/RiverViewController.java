package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.sim.RiverSystem;
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

    }

    // ====== Accessors ======

    public RiverRenderer getRiverRenderer() {
        return riverRenderer;
    }

    public void setRiverRenderer(RiverRenderer riverRenderer) {
        this.riverRenderer = riverRenderer;
    }
}
