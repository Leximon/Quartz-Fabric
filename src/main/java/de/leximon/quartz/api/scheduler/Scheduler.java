package de.leximon.quartz.api.scheduler;

import net.minecraft.server.MinecraftServer;

import java.util.HashSet;
import java.util.function.Consumer;

public class Scheduler {

    private final HashSet<Task> tasks = new HashSet<>();

    public Task runTask(Consumer<Task> consumer) {
        return runTaskLater(consumer, 0);
    }

    public Task runTaskLater(Consumer<Task> consumer, int delay) {
        return runTaskTimer(consumer, delay, Task.NO_REPEATING);
    }

    public Task runTaskTimer(Consumer<Task> consumer, int delay, int period) {
        Task task = new Task(delay, period, consumer);
        synchronized (tasks) {
            tasks.add(task);
        }
        return task;
    }

    public void tick(MinecraftServer server) {
        synchronized (tasks) {
            for (Task task : tasks)
                task.tick();
            tasks.removeIf(task -> !task.isRunning());
        }
    }

}
