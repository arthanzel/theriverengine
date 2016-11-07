package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.sim.environment.Environment;
import com.arthanzel.theriverengine.sim.influence.Influence;
import com.arthanzel.theriverengine.sim.RiverRunner;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.ui.controls.BeanEditPane;
import com.arthanzel.theriverengine.ui.controls.RadioField;
import com.arthanzel.theriverengine.ui.controls.TimeLabel;
import com.arthanzel.theriverengine.util.TextUtils;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class RiverViewController {
    @FXML private Accordion optionsAccordion;
    @FXML private RiverRenderer riverRenderer;
    @FXML private Label fpsLabel;
    @FXML private TimeLabel timeLabel;
    @FXML private TextField speedField;

    private DoubleProperty speed = new SimpleDoubleProperty(500);

    private RiverSystem system;
    private final Object systemLock = new Object();

    @SuppressWarnings("unused")
    public void initialize() {
        AnimationTimer anim = new AnimationTimer() {
            double skip = 0;

            @Override
            public void handle(long now) {
                synchronized (systemLock) {
                    timeLabel.setTime(system.getTime());
                    riverRenderer.update(system);
                }
            }
        };
        anim.start();

        riverRenderer.fpsProperty().addListener((observable, oldValue, newValue) -> {
            // FIXME: This event is coming from a different thread, which JavaFX does not like.
            //fpsLabel.setText(newValue + " FPS");
        });

        speedField.textProperty().addListener((observable, oldValue, newValue) -> {
            speed.setValue(Double.parseDouble(newValue));
        });
    }

    public void initialize(RiverSystem system, RiverRunner runner) {
        this.system = system;

        // ====== Create accordion panes for parameters ======

        // Render options
        optionsAccordion.getPanes().add(new BeanEditPane("Render Options", riverRenderer.getOptions()));

        // Environment selector
        TitledPane envSelPane = new TitledPane();
        envSelPane.setText("Render Environments");
        RadioField selector = new RadioField(system.getEnvironments().keySet().toArray(new String[0]));
        envSelPane.setContent(selector);
        optionsAccordion.getPanes().add(envSelPane);
        riverRenderer.renderableEnvironmentProperty().bind(selector.selectedProperty());

        // Environments
        for (String k : system.getEnvironments().keySet()) {
            Environment env = system.getEnvironments().get(k);
            optionsAccordion.getPanes().add(new BeanEditPane(TextUtils.toWords(k), env));
        }

        // Influences
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
        synchronized (systemLock) {
            this.system = system;
        }
    }

    public double getSpeed() {
        return speed.get();
    }

    public DoubleProperty speedProperty() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed.set(speed);
    }
}
