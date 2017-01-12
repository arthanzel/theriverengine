package com.arthanzel.theriverengine.sim;

import com.arthanzel.theriverengine.common.ui.binding.FileBinding;
import com.arthanzel.theriverengine.concurrent.QueueMode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

/**
 * Created by martin on 2017-01-10.
 */
public class RunnerOptions {
    private ObjectProperty<QueueMode> queueMode = new SimpleObjectProperty<>(QueueMode.BLOCK);
    private DoubleProperty reportingInterval = new SimpleDoubleProperty(5);

    @FileBinding(folders = true)
    private File dataDirectory;

    private String currentFile;
    private int initialAgents = 100;

    public RunnerOptions() {
        dataDirectory = new File(System.getProperty("user.home"), "theriverengine");

        String uuid = UUID.randomUUID().toString().substring(0, 4);
        currentFile = String.format("%s-%s.json",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH.mm.ss")),
                uuid);
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

    public File getDataDirectory() {
        return dataDirectory;
    }

    public void setDataDirectory(File dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }

    public int getInitialAgents() {
        return initialAgents;
    }

    public void setInitialAgents(int initialAgents) {
        this.initialAgents = initialAgents;
    }
}
