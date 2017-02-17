package com.arthanzel.theriverengine.common.ui;

import com.arthanzel.theriverengine.common.util.TimeUtils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    private DoubleProperty time = new SimpleDoubleProperty(Double.NEGATIVE_INFINITY);

    public TimeLabel() {
        time.addListener((observable, oldValue, newValue) -> {
            double seconds = (double) newValue;
            StringBuilder sb = new StringBuilder();
            sb.append("+ ");

            double years = TimeUtils.years(seconds);
            sb.append((int) years).append(" y, ");
            double days = TimeUtils.days(seconds) % 365;
            sb.append((int) days).append(" d, ");
            double hours = TimeUtils.hours(seconds) % 24;
            sb.append(String.format("%02d h, ", (int) hours));
            double minutes = TimeUtils.minutes(seconds) % 60;
            sb.append(String.format("%02d m, ", (int) minutes));

            sb.append(String.format("%05.2f s", seconds % 60));

            this.setText(sb.toString());
        });
        time.set(0);
    }

    public double getTime() {
        return time.get();
    }

    public DoubleProperty timeProperty() {
        return time;
    }

    public void setTime(double time) {
        this.time.set(time);
    }
}
