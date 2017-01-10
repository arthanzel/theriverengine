package com.arthanzel.theriverengine.util;

import javafx.application.Platform;

/**
 * Created by martin on 2017-01-02.
 */
public class FXTimer {
    private Thread t;
    private boolean flagForStop = false;

    private FXTimer(long ms, Runnable runnable) {
        t = new Thread(() -> {
            while (true) {
                if (flagForStop) {
                    break;
                }

                Platform.runLater(runnable);
                try {
                    Thread.sleep(ms);
                } catch (InterruptedException e) {

                }
            }
        });
        t.start();
    }

    public void stop() {
        flagForStop = true;
    }

    public static FXTimer setInterval(long ms, Runnable runnable) {
        return new FXTimer(ms, runnable);
    }
}
