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

/**
 * FieldEditor is a control that provides read and write access to a field of
 * some object. This field must provide either public getters/setters or a
 * public JavaFX property accessor.
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
     * Creates a new field editor.
     * This is generally bad and you should subclass FieldEditor.
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

                this.bindDirectional(this.value, prop);
            }
            catch (InvocationTargetException | IllegalAccessException | NullPointerException e) {
                throw new BindingInvocationException(field, bean.getClass());
            }
        }
        else {
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
                    if (!classEquals(newValue.getClass(), field.getType())) {
                        new TypeMismatchException(field).printStackTrace();
                        return;
                    }

                    try {
                        pd.getWriteMethod().invoke(bean, newValue);
                    }
                    catch (Exception e) {
                        // If setter does not exist. Property may be read-only.
                        // Not much we can do in an event handler...
                        System.err.println("Error: FieldEditor tried to write to field " + field.getName() + " with no setter.");
                    }
                });
            }
            catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new BindingInvocationException(field, bean.getClass());
            }
        }
    }
    /**
     * Sets up a bidirectional binding between two properties with
     * type-checking. This method prints to the standard error, since exceptions
     * are difficult to handle in event listeners.
     */
    private void bindDirectional(Property<T> p1, Property<T> p2) {
        p1.addListener((observable, oldValue, newValue) -> {
            if (classEquals(newValue.getClass(), p1.getValue().getClass())) {
                p2.setValue(newValue);
            }
            else {
                new TypeMismatchException(field).printStackTrace();
            }
        });
        p2.addListener((observable, oldValue, newValue) -> {
            if (classEquals(newValue.getClass(), p1.getValue().getClass())) {
                p1.setValue(newValue);
            }
            else {
                new TypeMismatchException(field).printStackTrace();
            }
        });
    }

    private boolean classEquals(Class<?> c1, Class<?> c2) {
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
