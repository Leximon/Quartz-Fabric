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
        Task task = new Task(delay, period, false, consumer);
        synchronized (tasks) {
            tasks.add(task);
        }
        return task;
    }

    public Task runTaskAsync(Consumer<Task> consumer) {
        return runTaskLaterAsync(consumer, 0);
    }

    public Task runTaskLaterAsync(Consumer<Task> consumer, int delay) {
        return runTaskTimerAsync(consumer, delay, Task.NO_REPEATING);
    }

    public Task runTaskTimerAsync(Consumer<Task> consumer, int delay, int period) {
        Task task = new Task(delay, period, true, consumer);
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
