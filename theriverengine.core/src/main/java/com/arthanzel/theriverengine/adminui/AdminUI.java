package com.arthanzel.theriverengine.adminui;

import com.arthanzel.theriverengine.common.ui.EnumComboBox;
import com.arthanzel.theriverengine.concurrent.QueueMode;
import com.arthanzel.theriverengine.sim.RiverRunner;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.RunnerOptions;
import com.arthanzel.theriverengine.ui.controls.BeanEditPane;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Queue;

/**
 * AdminUI is the graphical administration console where parameters of the
 * simulation may be adjusted.
 */
public class AdminUI extends Stage {
    private RiverRunner runner;
    private RiverSystem system;

    // region FXML
    @FXML
    private Button dataButton;

    @FXML
    private Accordion optionsAccordion;

    @FXML
    private ToggleButton playButton;
    // endregion

    private BooleanProperty playing = new SimpleBooleanProperty(true);

    public AdminUI(RiverRunner runner) throws IOException {
        this.runner = runner;
        this.system = runner.getSystem();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        this.setTitle("The River Engine - Admin UI");
        this.setScene(new Scene(root));
        this.show();
    }

    public void initialize() {
        optionsAccordion.getPanes().add(new RunnerOptionsPane(runner.getOptions()));

        playing.bindBidirectional(playButton.selectedProperty());
    }

    public void addBean(Object bean) {
        optionsAccordion.getPanes().add(new BeanEditPane(bean));
    }

    public boolean isPlaying() {
        return playing.get();
    }

    public BooleanProperty playingProperty() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing.set(playing);
    }
}
