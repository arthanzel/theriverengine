package com.arthanzel.theriverengine.ui;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RenderOptions {
    @BooleanBinding private boolean renderingAgents = true;
    @BooleanBinding private boolean renderingEnvironments = false;
    @BooleanBinding private boolean renderingNetwork = true;

    // ====== Accessors ======

    public boolean isRenderingAgents() {
        return renderingAgents;
    }

    public void setRenderingAgents(boolean renderingAgents) {
        this.renderingAgents = renderingAgents;
    }

    public boolean isRenderingEnvironments() {
        return renderingEnvironments;
    }

    public void setRenderingEnvironments(boolean renderingEnvironments) {
        this.renderingEnvironments = renderingEnvironments;
    }

    public boolean isRenderingNetwork() {
        return renderingNetwork;
    }

    public void setRenderingNetwork(boolean renderingNetwork) {
        this.renderingNetwork = renderingNetwork;
    }
}
