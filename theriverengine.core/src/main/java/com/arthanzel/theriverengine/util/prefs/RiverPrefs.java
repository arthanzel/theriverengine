package com.arthanzel.theriverengine.util.prefs;

import com.arthanzel.theriverengine.common.util.ReflectionUtils;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;

public class RiverPrefs {
    private Ini ini;

    public RiverPrefs(String resourceName) throws IOException {
        try {
            ini = new Ini(RiverPrefs.class.getResource(resourceName));
        } catch (IOException | NullPointerException e) {
            throw new IOException("Can't find file " + resourceName);
        }
    }

    public RiverPrefs(File file) throws IOException {
        ini = new Ini(file);
    }

    /**
     * Applies the preferences contained within this RiverPrefs to a collection of beans.
     */
    public void set(Collection<?> beans) {
        for (Profile.Section s : ini.values()) {
            String name = s.getName();
            Object bean = beans.stream().filter(o -> name.equalsIgnoreCase(o.getClass().getSimpleName())).findFirst().orElse(null);
            if (bean == null) {
                System.err.println("Can't find bean " + name + " to set parameters!");
                continue;
            }

            // Found bean, set parameters
            for (String k : s.keySet()) {
                try {
                    set(bean, k, s.get(k));
                }
                catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
    }

    private void set(Object bean, String fieldName, String val) throws Exception {
        try {
            Field field = bean.getClass().getDeclaredField(fieldName);
            Object valObj = ReflectionUtils.parsePrimitiveValue(val, field.getType());

            PropertyDescriptor pd = new PropertyDescriptor(fieldName, bean.getClass());
            pd.getWriteMethod().invoke(bean, valObj);
        }
        catch (Exception e) {
            throw new Exception("Can't set field " + fieldName + " on " + bean);
        }
    }
}
