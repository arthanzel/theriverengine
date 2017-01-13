package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.HBox;

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
public abstract class FieldEditor<T> extends HBox {
    private final Field field;
    private final Object bean;

    private ObjectProperty<T> value = new SimpleObjectProperty<>();

    @SuppressWarnings("unchecked")
    public FieldEditor(Field field, Object bean) throws BindingInvocationException {
        this.setSpacing(7);

        /*
        FieldEditor is *not* type-safe. Be VERY careful and make sure that bound
        fields' types match their annotations.
         */

        this.field = field;
        this.bean = bean;
        boolean isProperty = Property.class.isAssignableFrom(field.getType());

        if (isProperty) {
            /*
            If the backing field is a JavaFX property, try to set up a bidirectional
            binding so that subclasses of FieldEditor can simply set the value
            property. Assumes a public method with default naming.
             */

            try {
                Method propAccessor = ReflectionUtils.getPropertyMethod(field.getName(), bean.getClass());
                Property<T> prop = (Property<T>) Objects.requireNonNull(propAccessor).invoke(bean);
                value.set(prop.getValue());
                prop.bindBidirectional(value);
            }
//            catch (ClassCastException e) {
//                throw new TypeMismatchException(field);
//            }
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
                    try {
                        pd.getWriteMethod().invoke(bean, newValue);
                    }
                    catch (Exception e) {
                        // Not much we can do in an event handler...
                        System.err.println("Error: FieldEditor tried to write to field " + field.getName() + " with no setter.");
                    }
                });
            }
//            catch (ClassCastException e) {
//                throw new TypeMismatchException(field);
//            }
            catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                throw new BindingInvocationException(field, bean.getClass());
            }
        }
    }

    // ====== Accessors ======

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
