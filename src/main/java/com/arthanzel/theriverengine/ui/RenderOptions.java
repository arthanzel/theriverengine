package com.arthanzel.theriverengine.ui;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RenderOptions {
    @BooleanBinding private volatile boolean renderingAgents = true;
    @BooleanBinding private volatile boolean renderingNetwork = true;
    @DoubleBinding(min = -10, max = 10) private volatile double legendMin = 0.0;
    @DoubleBinding(min = -10, max = 10) private volatile double legendMax = 1.0;
    @DoubleBinding(min = 0.1, max = 20) private volatile double envTickInterval = 5;

    // ====== Accessors ======

    public boolean isRenderingAgents() {
        return renderingAgents;
    }

    public void setRenderingAgents(boolean renderingAgents) {
        this.renderingAgents = renderingAgents;
    }

    public boolean isRenderingNetwork() {
        return renderingNetwork;
    }

    public void setRenderingNetwork(boolean renderingNetwork) {
        this.renderingNetwork = renderingNetwork;
    }

    public double getLegendMin() {
        return legendMin;
    }

    public void setLegendMin(double legendMin) {
        this.legendMin = legendMin;
    }

    public double getLegendMax() {
        return legendMax;
    }

    public void setLegendMax(double legendMax) {
        this.legendMax = legendMax;
    }

    public double getEnvTickInterval() {
        return envTickInterval;
    }

    public void setEnvTickInterval(double envTickInterval) {
        this.envTickInterval = envTickInterval;
    }
}
