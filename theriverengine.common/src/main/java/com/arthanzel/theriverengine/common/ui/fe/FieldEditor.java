package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.ClassUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

//TODO: fix these fucking comments

/**
 * FieldEditor is a control that provides read and write access to a field of
 * some object. This field must provide either public getters/setters or a
 * public JavaFX property accessor. <p>
 * <p>
 * <p>FieldEditor is a concrete class to
 * facilitate testing, but instantiating it directly is discouraged. Instead, it
 * is recommended to subclass it and adapt it for different cases. FieldEditor
 * on its own works simply as a wrapper around a field or property, and exposes
 * a JavaFX property.</p> <p> FieldEditor can wrap either a POJO field or a
 * JavaFX property. In the former case, ensure that a getter with the default
 * name is available for the field. A setter is optional if the field is meant
 * to be read-only, but FieldEditor will throw a runtime exception if you try to
 * set a value to a field that is missing a setter. In the latter case (JavaFX
 * property), ensure that an accessor with the default name is available for the
 * property. Property accessors for field {@code foo} have default name {@code
 * fooProperty()}.</p>
 *
 * @author Martin
 */
public class FieldEditor<T> extends HBox {
    private Field field;
    private Object bean;

    // Type of underlying field or property.
    private Class type;

    private ObjectProperty<T> value = new SimpleObjectProperty<>();

    /**
     * Instantiates a new FieldEditor.
     * @param field Field name to wrap.
     * @param bean Instance of object containing the field.
     * @param template Type (K) of the underlying field.
     * @param <K> Type of the underlying field. Must extend T.
     * @throws BindingInvocationException Thrown if FieldEditor can't access field accessors.
     * @throws TypeMismatchException Thrown if the provided type doesn't match the type of the underlying field.
     */
    public <K extends T> FieldEditor(Field field, Object bean, Class<K> template) throws BindingInvocationException, TypeMismatchException {
        this.field = field;
        this.bean = bean;
        boolean isProperty = ReflectionUtils.isProperty(field);

        // Ensure type-safety!
        // The type of the underlying field *must match* the generic type given in template.
        try {
            this.type = ReflectionUtils.getPropertyType(field, bean);
        } catch (InvocationTargetException e) {
            // Can't access the property accessor on the field.
            throw new BindingInvocationException(field, bean.getClass());
        }
        if (!ClassUtils.isAssignable(this.type, template, true)) {
            // Type mismatch!
            throw new TypeMismatchException(field);
        }

        this.setSpacing(7);
        this.setAlignment(Pos.CENTER_LEFT);

        /*
        FieldEditor is *not* type-safe. Be VERY careful and make sure that bound
        fields' types match their annotations.
         */

        if (isProperty) {
            /*
            If the backing field is a JavaFX property, try to set up a bidirectional
            binding so that subclasses of FieldEditor can simply set the value
            property. Assumes a public method with default naming.
             */

            try {
                Method propAccessor = ReflectionUtils.getPropertyMethod(field.getName(), bean.getClass());
                Property<T> prop = (Property<T>) Objects.requireNonNull(propAccessor).invoke(bean);
                this.value.set(prop.getValue());

                this.bindBidirectional(this.value, prop);
            } catch (InvocationTargetException | IllegalAccessException | NullPointerException e) {
                throw new BindingInvocationException(field, bean.getClass());
            }
        } else {
            /*
            If the backing field is a POJO property, try adding listeners that
            fire the get/set methods of the POJO. This binding is
            unidirectional. The FieldEditor will *not* pick up changes to the
            underlying field, and will try to overwrite it even if is not in
            sync.
             */

            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), bean.getClass());
                T val = (T) pd.getReadMethod().invoke(bean);

                value.set(val);
                value.addListener((observable, oldValue, newValue) -> {
                    if (!classIs(newValue.getClass(), field.getType())) {
                        new TypeMismatchException(field).printStackTrace();
                        return;
                    }

                    try {
                        pd.getWriteMethod().invoke(bean, newValue);
                    } catch (Exception e) {
                        // If setter does not exist. Property may be read-only.
                        // Not much we can do in an event handler...
                        System.err.println("Error: FieldEditor tried to write to field " + field.getName() + " with no setter.");
                    }
                });
            } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new BindingInvocationException(field, bean.getClass());
            }
        }
    }

    /**
     * Sets up a bidirectional binding between two properties with
     * type-checking. This method prints to the standard error, since exceptions
     * are difficult to handle in event listeners.
     */
    private void bindBidirectional(Property<T> p1, Property<T> p2) {
        p1.addListener((observable, oldValue, newValue) -> {
            if (classIs(newValue.getClass(), p2.getValue().getClass())) {
                p2.setValue(newValue);
            } else {
                new TypeMismatchException(field).printStackTrace();
            }
        });
        p2.addListener((observable, oldValue, newValue) -> {
            if (classIs(newValue.getClass(), p1.getValue().getClass())) {
                p1.setValue(newValue);
            } else {
                new TypeMismatchException(field).printStackTrace();
            }
        });
    }

    private boolean classIs(Class<?> c1, Class<?> c2) {
        return ClassUtils.isAssignable(c1, c2, true);
    }

    // ====== Accessors ======

    protected Class getType() {
        return type;
    }

    public Object getBean() {
        return bean;
    }

    public Field getField() {
        return field;
    }

    public ObjectProperty<T> valueProperty() {
        return value;
    }

    public T getValue() {
        return value.get();
    }

    public void setValue(T value) {
        this.value.set(value);
    }
}
