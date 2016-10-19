package com.arthanzel.theriverengine.ui;

import com.arthanzel.theriverengine.sim.influence.Influence;
import com.arthanzel.theriverengine.util.ReflectionUtils;
import com.arthanzel.theriverengine.util.TextUtils;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public class InfluencePane extends TitledPane {
    private Influence influence;

    public InfluencePane(Influence influence) {
        this.influence = influence;

        try {
            initUI();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initializing options!");
            System.exit(1);
        }
    }

    private void initUI() throws IntrospectionException {
        this.setText(influence.getName());

        VBox container = new VBox(7);
        this.setContent(container);

        List<Field> fields = ReflectionUtils.getAllDeclaredFields(this.influence.getClass());
        fields.sort((o1, o2) ->
            o1.getName().compareTo(o2.getName())
        );

        for (Field f : fields) {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), influence.getClass());

                if (f.isAnnotationPresent(DoubleBinding.class)) {
                    DoubleBinding annotation = f.getAnnotation(DoubleBinding.class);
                    initDouble(container, pd, annotation);
                }
                else if (f.isAnnotationPresent(BooleanBinding.class)) {
                    initBoolean(container, pd);
                }
            } catch (Exception e) {
                // Do nothing. pd probably refers to a non-property.
            }
        }
    }

    // TODO: Externalize these to custom widgets like BooleanEditor, DoubleEditor, etc.

    private void initBoolean(Pane container, PropertyDescriptor pd) throws InvocationTargetException, IllegalAccessException {
        boolean val = (boolean) pd.getReadMethod().invoke(influence);
        CheckBox checkbox = new CheckBox(TextUtils.toWords(pd.getName()));
        checkbox.setSelected(val);
        container.getChildren().add(checkbox);

        checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                pd.getWriteMethod().invoke(influence, newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initDouble(Pane container, PropertyDescriptor pd, DoubleBinding annotation) throws InvocationTargetException, IllegalAccessException {
        double val = (double) pd.getReadMethod().invoke(influence);

        DynamicLabel label = new DynamicLabel(TextUtils.toWords(pd.getName()), val);
        container.getChildren().add(label);

        Slider slider = new Slider(annotation.min(), annotation.max(), val);
        container.getChildren().add(slider);

        // On set event handler
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                pd.getWriteMethod().invoke(influence, newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        label.valueProperty().bind(slider.valueProperty());
    }
}