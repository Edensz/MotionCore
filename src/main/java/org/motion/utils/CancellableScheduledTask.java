package org.motion.utils;

import lombok.Setter;

import java.util.concurrent.ScheduledFuture;


// Util by: zLofro
public abstract class CancellableScheduledTask implements Runnable {
    private @Setter ScheduledFuture<?> scheduledFuture;

    public void cancel() {
        if (scheduledFuture == null) throw new IllegalStateException("The scheduled future for this task is not defined. It must be provided for the task to be cancelled.");
        scheduledFuture.cancel(true);
    }

}
