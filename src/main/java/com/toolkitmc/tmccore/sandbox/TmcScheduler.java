package com.toolkitmc.tmccore.sandbox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TmcScheduler {

    private static final List<ScheduledTask> tasks = new ArrayList<>();
    private static int currentTick = 0;

    public static void tick() {
        currentTick++;
        Iterator<ScheduledTask> it = tasks.iterator();
        while (it.hasNext()) {
            ScheduledTask task = it.next();
            task.ticksLeft--;
            if (task.ticksLeft <= 0) {
                task.runnable.run();
                it.remove();
            }
        }
    }

    public static void schedule(Runnable task, int delayTicks) {
        tasks.add(new ScheduledTask(task, delayTicks));
    }

    public static int getCurrentTick() {
        return currentTick;
    }

    private static class ScheduledTask {
        Runnable runnable;
        int ticksLeft;

        ScheduledTask(Runnable runnable, int delay) {
            this.runnable = runnable;
            this.ticksLeft = delay;
        }
    }
}