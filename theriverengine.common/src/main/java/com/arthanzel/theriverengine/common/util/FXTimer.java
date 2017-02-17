package com.arthanzel.theriverengine.common.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 * FXTimer runs a task at a specified interval.
 */
public class FXTimer {
    public static Timeline setInterval(double seconds, Runnable task) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(seconds), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                task.run();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        return timeline;
    }
}
