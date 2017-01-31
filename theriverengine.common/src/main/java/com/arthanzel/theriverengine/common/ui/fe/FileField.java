package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.ui.binding.FileBinding;
import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * FileField allows the user to select files or folders.
 *
 * @author Martin
 */
public class FileField extends FieldEditor<File> {
    public FileField(Field field, Object bean) throws BindingInvocationException, TypeMismatchException {
        super(field, bean, File.class);
        FileBinding annotation = field.getAnnotation(FileBinding.class);
        boolean folders = annotation.folders();

        VBox vbox = new VBox();
        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Hyperlink changeLink = new Hyperlink("Change");
        Hyperlink openLink = new Hyperlink(folders ? "Open" : "Open Folder");
        hbox.getChildren().addAll(
                new Label(ReflectionUtils.getBoundName(field) + ": "),
                changeLink,
                openLink);
        Label fileLabel = new Label(getValue().getAbsolutePath());
        this.valueProperty().addListener((observable, oldValue, newValue) -> {
            fileLabel.setText(newValue.getAbsolutePath());
        });
        vbox.getChildren().addAll(hbox, fileLabel);
        this.getChildren().add(vbox);

        // Clicking the hyperlink displays the file chooser
        changeLink.setOnAction(event -> {
            if (folders) {
                DirectoryChooser dc = new DirectoryChooser();
                File file = dc.showDialog(this.getScene().getWindow());
                if (file != null) {
                    this.setValue(file);
                }
            }
            else {
                FileChooser fc = new FileChooser();
                File file = fc.showOpenDialog(this.getScene().getWindow());
                if (file != null) {
                    this.setValue(file);
                }
            }
        });

        openLink.setOnAction(event -> {
            File file = getValue();
            try {
                if (file.isFile()) {
                    Desktop.getDesktop().open(file.getParentFile());
                }
                else {
                    Desktop.getDesktop().open(file);
                }
            }
            catch (Exception e) {
                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        "Could not open " + file.getAbsolutePath(),
                        ButtonType.OK
                );
                alert.showAndWait();
            }
        });
    }
}
