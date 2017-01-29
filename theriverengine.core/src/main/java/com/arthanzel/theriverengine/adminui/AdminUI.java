package com.arthanzel.theriverengine.adminui;

import com.arthanzel.theriverengine.common.ui.binding.Bindings;
import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import com.arthanzel.theriverengine.gui.RiverView;
import com.arthanzel.theriverengine.sim.RiverRunner;
import com.arthanzel.theriverengine.sim.RiverSystem;
import com.arthanzel.theriverengine.sim.influence.Influence;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

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

    @FXML
    private Button guiButton;
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

        // Construct panes for influences
        for (Influence i : runner.getInfluences()) {
            addBean(i);
        }

        playing.bindBidirectional(playButton.selectedProperty());

        guiButton.setOnAction(event -> {
            RiverView view = new RiverView(runner.getSystem().getNetwork());
            view.show();
            Consumer<String> consumer = view::updateModel;
            runner.getReporter().getConsumers().add(consumer);
            view.setOnCloseRequest((ev) -> runner.getReporter().getConsumers().remove(consumer));
        });
    }

    private void addBean(Object bean) {
        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(Bindings.createForBean(bean));
        optionsAccordion.getPanes().add(new TitledPane(
                ReflectionUtils.getBoundName(bean),
                vbox
        ));
    }

    public void wireToRunner() {
        playing.addListener((observable, oldValue, newValue) -> {
            runner.setEnabled(newValue);
        });
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
