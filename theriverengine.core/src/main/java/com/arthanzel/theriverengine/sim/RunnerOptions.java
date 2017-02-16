package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.common.ui.binding.*;
import com.arthanzel.theriverengine.concurrent.QueueMode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Contains several options for configuring the RiverRunner.
 */
public class RunnerOptions {
    @EnumBinding
    private ObjectProperty<QueueMode> queueMode = new SimpleObjectProperty<>(QueueMode.BLOCK);

    @DoubleSpinnerBinding(min = 0.25, max = 10)
    private DoubleProperty reportingInterval = new SimpleDoubleProperty(2);

    public RunnerOptions() {

    }

    public QueueMode getQueueMode() {
        return queueMode.get();
    }

    public ObjectProperty<QueueMode> queueModeProperty() {
        return queueMode;
    }

    public void setQueueMode(QueueMode queueMode) {
        this.queueMode.set(queueMode);
    }

    public double getReportingInterval() {
        return reportingInterval.get();
    }

    public DoubleProperty reportingIntervalProperty() {
        return reportingInterval;
    }

    public void setReportingInterval(double reportingInterval) {
        this.reportingInterval.set(reportingInterval);
    }
}
