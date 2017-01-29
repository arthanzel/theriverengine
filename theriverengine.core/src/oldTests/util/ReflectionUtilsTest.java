package com.arthanzel.theriverengine.util;

import com.arthanzel.theriverengine.ui.BindingName;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by martin on 2016-10-26.
 */
public class ReflectionUtilsTest {
    @Test
    public void getAllDeclaredFields() throws Exception {
        List<Field> fields = ReflectionUtils.getAllDeclaredFields(Child.class);
        assertTrue(3 < fields.size());
        assertTrue(fields.containsAll(Arrays.asList(Child.class.getDeclaredFields())));
        assertTrue(fields.containsAll(Arrays.asList(ParentClass.class.getDeclaredFields())));

        // Assert order
        int p = fields.indexOf(ParentClass.class.getDeclaredField("parentField"));
        int c1 = fields.indexOf(Child.class.getDeclaredField("childField1"));
        int c2 = fields.indexOf(Child.class.getDeclaredField("childField2"));
        assertTrue(c2 < c1);
        assertTrue(c2 < p);
    }

    @Test
    public void getBeanName() throws Exception {
        assertEquals("Parent Class", ReflectionUtils.getBeanName(new ParentClass()));
        assertEquals("Class that is a child", ReflectionUtils.getBeanName(new Child()));
        assertEquals("Parent Field", ReflectionUtils.getBeanName(ParentClass.class.getDeclaredField("parentField")));
        assertEquals("Field that is a child", ReflectionUtils.getBeanName(Child.class.getDeclaredField("childField2")));
        assertEquals("Child Field 1", ReflectionUtils.getBeanName(Child.class.getDeclaredField("childField1")));
    }

    private class ParentClass {
        private int parentField;
    }

    @BindingName("Class that is a child")
    private class Child extends ParentClass {
        @BindingName("Field that is a child")
        private int childField2;
        private int childField1;
    }
}