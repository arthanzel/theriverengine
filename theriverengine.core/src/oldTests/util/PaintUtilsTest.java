package com.arthanzel.theriverengine.util;

import javafx.scene.paint.Stop;
import org.junit.Test;

/**
 * Created by Martin on 2016-12-01.
 */
public class PaintUtilsTest {
    @Test
    public void hsbGradient() throws Exception {
        Stop[] stops = PaintUtils.hueGradient(30, 140);
    }
}