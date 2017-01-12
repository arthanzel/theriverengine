package com.arthanzel.theriverengine.ui.controls;

import com.arthanzel.theriverengine.common.ui.fe.BooleanValueEditor;
import com.arthanzel.theriverengine.common.ui.binding.BooleanBinding;
import com.arthanzel.theriverengine.common.ui.binding.SliderBinding;
import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import com.arthanzel.theriverengine.common.util.TextUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
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
        this.setText(title != null ? title : ReflectionUtils.getBoundName(bean));

        VBox container = new VBox(7);
        this.setContent(container);

        // Add field editors
        List<Field> fields = ReflectionUtils.getAllDeclaredFields(this.bean.getClass());
        fields.sort(Comparator.comparing(Field::getName));
        for (Field f : fields) {
            try {
                if (f.isAnnotationPresent(SliderBinding.class)) {
                    container.getChildren().add(new RealValueEditor(f, bean, f.getAnnotation(SliderBinding.class)));
                }
                else if (f.isAnnotationPresent(BooleanBinding.class)) {
                    container.getChildren().add(new BooleanValueEditor(f, bean));
                }
            } catch (Exception e) {
                // Do nothing. pd probably refers to a non-property that is missing getters/setters.
            }
        }

        // Add method buttons
        List<Method> methods = Arrays.asList(this.bean.getClass().getMethods());
        methods.sort(Comparator.comparing(Method::getName));
        for (Method m : methods) {
            if (m.getName().startsWith("fire")) {
                Button b = new Button();
                b.setText(TextUtils.toWords(m.getName()));
                b.setOnAction((actionEvent) -> {
                    try {
                        m.invoke(this.bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                container.getChildren().add(b);
            }
        }
    }
}