package com.arthanzel.theriverengine.ui.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class NumberPrompt extends Stage {
    private String prompt;
    private EventHandler<ActionEvent> submitHandler;

    @FXML Label label;
    @FXML TextInputControl textInput;

    public NumberPrompt(String prompt) {
        this.initModality(Modality.WINDOW_MODAL);
        this.prompt = prompt;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NumberPrompt.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            this.setScene(scene);
        } catch (Exception e) {
            System.err.println("NumberPrompt.fxml seems to be missing...");
            e.printStackTrace();
        }
    }

    public void initialize() {
        label.setText(prompt);
    }

    @FXML public void onKeyPressed(KeyEvent ev) {
        if (ev.getCode() == KeyCode.ENTER) {
            onOKPressed(null);
        }
    }

    @FXML public void onOKPressed(ActionEvent ev) {
        try {
            double val = Double.parseDouble(textInput.getText());
            if (submitHandler != null) {
                submitHandler.handle(new ActionEvent(val, null));
            }
            this.close();
        }
        catch (Exception e) {
            textInput.setStyle("-fx-border-color: red");
        }
    }

    @FXML public void onCancelPressed(ActionEvent ev) {
        this.close();
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public EventHandler<ActionEvent> getSubmitHandler() {
        return submitHandler;
    }

    public void setSubmitHandler(EventHandler<ActionEvent> submitHandler) {
        this.submitHandler = submitHandler;
    }
}
