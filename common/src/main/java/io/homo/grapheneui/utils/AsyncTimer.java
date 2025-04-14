package io.homo.grapheneui.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncTimer {
    private final ScheduledExecutorService scheduler;

    public AsyncTimer() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public AsyncTimer schedule(Runnable task, long delay, TimeUnit unit) {
        scheduler.schedule(() -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delay, unit);
        return this;
    }

    public AsyncTimer scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, initialDelay, period, unit);
        return this;
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}