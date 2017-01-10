package com.arthanzel.theriverengine.ui.controls;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.Label;

import java.util.LinkedList;

/**
 * Custom label control that displays a textual representation of some time in milliseconds.
 * Displays (if applicable) the number of years, days, hours, minutes, and seconds.
 *
 * @author Martin
 */
public class TimeLabel extends Label {
    private LongProperty time = new SimpleLongProperty(0);

    public TimeLabel() {
        time.addListener((observable, oldValue, newValue) -> {
            LinkedList<String> sb = new LinkedList<String>();
            long t = (long) newValue;

            long seconds = t / 1000;
            sb.addFirst(seconds % 60 + " s");

            do {
                long minutes = seconds / 60;
                if (minutes == 0) break;
                sb.addFirst(minutes % 60 + " min,");

                long hours = minutes / 60;
                if (hours == 0) break;
                sb.addFirst(hours % 24 + " hrs,");

                long days = hours / 24;
                if (days == 0) break;
                sb.addFirst(days % 365 + " days,");

                long years = days / 365;
                if (years == 0) break;
                sb.addFirst(years + " years,");
            } while (false);

            sb.addFirst("+");

            this.setText(String.join(" ", sb));
        });
    }

    public long getTime() {
        return time.get();
    }

    public LongProperty timeProperty() {
        return time;
    }

    public void setTime(long time) {
        this.time.set(time);
    }
}
