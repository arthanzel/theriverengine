package com.arthanzel.theriverengine.ui.controls;

import com.arthanzel.theriverengine.ui.BooleanBinding;
import com.arthanzel.theriverengine.ui.DoubleBinding;
import com.arthanzel.theriverengine.util.ReflectionUtils;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * BeanEditPane is a TitledPane that fits into an accordion. It allows JavaBean's annotated with *Binding annotations to
 * be accessed and modified directly from a UI.
 *
 * @author Martin
 */
public class BeanEditPane extends TitledPane {
    private Object bean;

    /**
     * Constructs a new BeanEditPane representing some annotated bean.
     * @param bean Bean to bind to this BeanEditPane.
     */
    public BeanEditPane(Object bean) {
        this(null, bean);
    }

    /**
     * Constructs a new BeanEditPane with a title representing some annotated bean.
     * @param title Title of the edit pane.
     * @param bean Bean to bind to this BeanEditPane.
     */
    public BeanEditPane(String title, Object bean) {
        this.bean = bean;

        try {
            initUI(title);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initializing options!");
            System.exit(1);
        }
    }

    private void initUI(String title) throws IntrospectionException {
        // Sets the text visible in the accordion
        this.setText(title != null ? title : ReflectionUtils.getBeanName(bean));

        VBox container = new VBox(7);
        this.setContent(container);

        List<Field> fields = ReflectionUtils.getAllDeclaredFields(this.bean.getClass());
        //fields.
        fields.sort((o1, o2) ->
            o1.getName().compareTo(o2.getName())
        );

        for (Field f : fields) {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), bean.getClass());

                if (f.isAnnotationPresent(DoubleBinding.class)) {
                    container.getChildren().add(new RealValueEditor(f, bean, f.getAnnotation(DoubleBinding.class)));
                }
                else if (f.isAnnotationPresent(BooleanBinding.class)) {
                    container.getChildren().add(new BooleanValueEditor(f, bean));
                }
            } catch (Exception e) {
                // Do nothing. pd probably refers to a non-property that is missing getters/setters.
            }
        }
    }
}