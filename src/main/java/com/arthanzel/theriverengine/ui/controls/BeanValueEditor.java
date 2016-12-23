package com.arthanzel.theriverengine.ui.controls;

import javafx.scene.layout.VBox;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * TODO: Documentation
 *
 * @author Martin
 */
public abstract class BeanValueEditor<T> extends VBox {
    private final Field field;
    private final PropertyDescriptor propertyDescriptor;
    private final Object bean;

    public BeanValueEditor(Field field, Object bean) throws IntrospectionException {
        this.field = field;
        this.bean = bean;
        this.propertyDescriptor = new PropertyDescriptor(field.getName(), bean.getClass());
    }

    public T getValue() throws InvocationTargetException, IllegalAccessException {
        //FIXME: Fix this warning
        return (T) propertyDescriptor.getReadMethod().invoke(bean);
    }

    public void setValue(T val) throws InvocationTargetException, IllegalAccessException {
        propertyDescriptor.getWriteMethod().invoke(bean, val);
    }

    // ====== Accessors ======

    public Object getBean() {
        return bean;
    }

    public Field getField() {
        return field;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }
}
