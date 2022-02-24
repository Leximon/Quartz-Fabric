package de.leximon.quartz.api.scheduler;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class Task {

    public static final int NO_REPEATING = -1;

    private final Consumer<Task> consumer;
    private final int period;
    private int time;
    private boolean running = true;

    public Task(int delay, int period, Consumer<Task> consumer) {
        this.consumer = consumer;
        this.period = period;
        this.time = delay;
    }

    public void run() {
        consumer.accept(this);
    }

    public void cancel() {
        running = false;
    }

    public void tick() {
        if(!running)
            return;
        if(time <= 0) {
            run();
            if (period == -1) {
                running = false;
                return;
            }
            time = period;
        }
        time--;
    }
}
