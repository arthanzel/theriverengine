package com.arthanzel.theriverengine.common.ui.fe;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by martin on 2017-01-30.
 */
public class FieldEditorTest {
    @Test
    public void testFieldEditor() {
        try {
            FieldEditor<Integer> i1Editor = new FE<>(this.getClass().getDeclaredField("intValue"), this, Integer.class);
            FieldEditor<Integer> i2Editor = new FE<>(this.getClass().getDeclaredField("int2Value"), this, Integer.class);

            assertEquals(i1Editor.getValue().intValue(), 2);
            assertEquals(i2Editor.getValue().intValue(), 2);

            setIntValue(3);
            setInt2Value(3);
            assertEquals(i1Editor.getValue().intValue(), 2);
            assertEquals(i2Editor.getValue().intValue(), 3);

            i1Editor.setValue(4);
            i2Editor.setValue(4);
            assertEquals(i1Editor.getValue().intValue(), 4);
            assertEquals(i2Editor.getValue().intValue(), 4);
            assertEquals(intValue, 4);
            assertEquals(int2Value.getValue().intValue(), 4);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFieldMismatch() {
        try {
            FieldEditor<String> str = new FieldEditor<>(field("intValue"), this, String.class);
        }
        catch (TypeMismatchException e) {
            return;
        }
        catch (Exception e) {
            fail();
        }
        fail();
    }

    private Field field(String name) {
        try {
            return this.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            fail();
        }
        return null;
    }

    private class FE<T> extends FieldEditor<T> {
        FE(Field f, Object bean, Class<T> template) throws BindingInvocationException, TypeMismatchException {
            super(f, bean, template);
        }
    }

    private int intValue = 2;
    private IntegerProperty int2Value = new SimpleIntegerProperty(2);

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int doubleValue) {
        this.intValue = doubleValue;
    }

    public int getInt2Value() {
        return int2Value.get();
    }

    public IntegerProperty int2ValueProperty() {
        return int2Value;
    }

    public void setInt2Value(int int2Value) {
        this.int2Value.set(int2Value);
    }
}