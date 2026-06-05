package com.toolkitmc.tmccore.scheduler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TmcScheduler {
    private final List<ScheduledTask> tasks = new ArrayList<>();

    public TmcScheduler() {
        ServerTickEvents.END_SERVER_TICK.register(this::tick);
    }

    public void schedule(Runnable task, int delayTicks) {
        tasks.add(new ScheduledTask(task, delayTicks));
    }

    private void tick(MinecraftServer server) {
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

    private static class ScheduledTask {
        Runnable runnable;
        int ticksLeft;

        ScheduledTask(Runnable runnable, int delay) {
            this.runnable = runnable;
            this.ticksLeft = delay;
        }
    }
}