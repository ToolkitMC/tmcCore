package com.toolkitmc.core.util;

import com.toolkitmc.core.TmCore;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Server-tick based task scheduler.
 *
 * <p>Schedule one-shot or repeating tasks relative to the current server tick.
 *
 * <pre>
 *   // Run once after 20 ticks (1 second)
 *   TmScheduler.runLater(20, () -> TmCore.LOGGER.info("1 second passed!"));
 *
 *   // Run every 100 ticks
 *   TmScheduler.TaskHandle handle = TmScheduler.runRepeating(100, () -> updateSomething());
 *
 *   // Cancel
 *   handle.cancel();
 * </pre>
 *
 * <p>Call {@link #init()} once during {@code onInitialize}.
 */
public final class TmScheduler {

    private static final List<Task> tasks = new CopyOnWriteArrayList<>();
    private static long currentTick = 0L;
    private static boolean initialized = false;

    private TmScheduler() {}

    /** Registers the Fabric ServerTickEvents callback. Called by TmCore. */
    public static void init() {
        if (initialized) return;
        ServerTickEvents.END_SERVER_TICK.register(TmScheduler::onServerTick);
        initialized = true;
    }

    private static void onServerTick(MinecraftServer server) {
        currentTick++;
        Iterator<Task> iter = tasks.iterator();
        while (iter.hasNext()) {
            Task task = iter.next();
            if (task.cancelled) {
                tasks.remove(task);
                continue;
            }
            if (currentTick >= task.nextRunTick) {
                try {
                    task.action.run();
                } catch (Exception e) {
                    TmCore.LOGGER.error("TmScheduler task threw exception: {}", e.getMessage(), e);
                }
                if (task.periodTicks <= 0) {
                    tasks.remove(task);
                } else {
                    task.nextRunTick = currentTick + task.periodTicks;
                }
            }
        }
    }

    /**
     * Schedules a one-shot task to run after {@code delayTicks} ticks.
     */
    public static void runLater(long delayTicks, Runnable action) {
        tasks.add(new Task(action, currentTick + delayTicks, 0));
    }

    /**
     * Schedules a repeating task with an initial delay and period.
     *
     * @return a handle to cancel the task
     */
    public static TaskHandle runRepeating(long initialDelayTicks, long periodTicks, Runnable action) {
        Task task = new Task(action, currentTick + initialDelayTicks, periodTicks);
        tasks.add(task);
        return () -> task.cancelled = true;
    }

    /**
     * Schedules a repeating task starting after {@code periodTicks} ticks.
     */
    public static TaskHandle runRepeating(long periodTicks, Runnable action) {
        return runRepeating(periodTicks, periodTicks, action);
    }

    /** Cancels all scheduled tasks. */
    public static void cancelAll() {
        tasks.forEach(t -> t.cancelled = true);
        tasks.clear();
    }

    /** Returns the total number of active (non-cancelled) scheduled tasks. */
    public static int activeTaskCount() {
        return (int) tasks.stream().filter(t -> !t.cancelled).count();
    }

    @FunctionalInterface
    public interface TaskHandle {
        void cancel();
    }

    private static final class Task {
        final Runnable action;
        long nextRunTick;
        final long periodTicks;
        volatile boolean cancelled = false;

        Task(Runnable action, long nextRunTick, long periodTicks) {
            this.action = action;
            this.nextRunTick = nextRunTick;
            this.periodTicks = periodTicks;
        }
    }
}
