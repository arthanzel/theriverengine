package com.arthanzel.theriverengine.util.prefs;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class RiverPrefsTest {
    @Test
    public void testRiverPrefs() throws IOException {
        RiverPrefs rp = new RiverPrefs("/prefs/testPrefs.ini");
        RiverPrefsObject rpo = new RiverPrefsObject();
        assertEquals(rpo.getStringObj(), "foo");
        rp.set(Arrays.asList(new Object[] { rpo }));
        assertEquals(rpo.getStringObj(), "bar");
        assertEquals(rpo.getDoubleObj(), 2, 0.01);
        assertEquals(rpo.getIntObj(), 2);
        assertTrue(rpo.isBooleanObj());
    }
}