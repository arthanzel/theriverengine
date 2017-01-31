package com.arthanzel.theriverengine.common.ui.fe;

import com.arthanzel.theriverengine.common.testutil.JavaFXThreadingRule;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.DayOfWeek;

import static org.junit.Assert.*;

/**
 * Created by martin on 2017-01-30.
 */
public class EnumComboFieldTest {
    @Rule
    public JavaFXThreadingRule jfxr = new JavaFXThreadingRule();

    private Letter l = Letter.A;
    // lp == letter property
    private ObjectProperty<Letter> lp = new SimpleObjectProperty<>(Letter.A);

    @Test
    public void testEnumComboField() throws BindingInvocationException, TypeMismatchException {
        EnumComboField fieldFE = new EnumComboField(field("l"), this, Enum.class);
        EnumComboField propFE = new EnumComboField(field("lp"), this, Enum.class);
        assertEquals(fieldFE.getValue(), Letter.A);
        assertEquals(propFE.getValue(), Letter.A);

        l = Letter.B;
        lp.setValue(Letter.B);
        assertEquals(fieldFE.getValue(), Letter.A);
        assertEquals(propFE.getValue(), Letter.B);

        fieldFE.setValue(Letter.C);
        propFE.setValue(Letter.C);
        assertEquals(l, Letter.C);
        assertEquals(lp.getValue(), Letter.C);
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