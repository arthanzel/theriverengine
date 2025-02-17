package com.arthanzel.theriverengine.gui.util;

import com.arthanzel.theriverengine.common.util.FishMath;

import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;

/**
 * Created by martin on 2016-11-02.
 */
public class UIUtils {
    public static final Stop[] HSB_STOPS = hueGradient(0, 360);

    public static void enableClip(Region region) {
        Rectangle clip = new Rectangle(region.getWidth(), region.getHeight());
        clip.widthProperty().bind(region.widthProperty());
        clip.heightProperty().bind(region.heightProperty());
        region.setClip(clip);
    }

    public static final Stop[] hueGradient(double fromHue, double toHue) {
        // Stop hues are multiples of 60
        LinkedList<Stop> stops = new LinkedList<>();
        stops.add(new Stop(0, Color.hsb(fromHue, 1, 1)));

        if (fromHue < toHue) {
            double nextHue = FishMath.floor(fromHue, 60) + 60;
            for (double h = nextHue; h < toHue; h += 60) {
                stops.add(new Stop(FishMath.inverseLerp(fromHue, toHue, h), Color.hsb(h, 1.0, 1.0)));
            }
        }
        else {
            double nextHue = FishMath.ceil(fromHue, 60) - 60;
            for (double h = nextHue; h > toHue; h -= 60) {
                stops.add(new Stop(FishMath.inverseLerp(fromHue, toHue, h), Color.hsb(h, 1.0, 1.0)));
            }
        }


        stops.add(new Stop(1, Color.hsb(toHue, 1, 1)));
        return stops.toArray(new Stop[0]);
    }
}
