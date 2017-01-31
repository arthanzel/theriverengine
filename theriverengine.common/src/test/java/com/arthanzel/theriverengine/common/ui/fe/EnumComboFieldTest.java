package com.arthanzel.theriverengine.common.ui.fe;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by martin on 2017-01-30.
 */
public class EnumComboFieldTest {
    Letter l = Letter.A;
    // lp == letter property
    ObjectProperty<Letter> lp = new SimpleObjectProperty<>(Letter.A);

    @Test
    public void testEnumComboField() throws BindingInvocationException, TypeMismatchException {
        EnumComboField fieldFE = new EnumComboField(field("l"), this, Letter.class);
        EnumComboField propFE = new EnumComboField(field("lp"), this, Letter.class);

    }

    public Letter getL() {
        return l;
    }

    public void setL(Letter l) {
        this.l = l;
    }

    public Letter getLp() {
        return lp.get();
    }

    public ObjectProperty<Letter> lpProperty() {
        return lp;
    }

    public void setLp(Letter lp) {
        this.lp.set(lp);
    }

    private enum Letter {
        A, B, C
    }

    private Field field(String name) {
        try {
            return this.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            fail();
        }
        return null;
    }
}