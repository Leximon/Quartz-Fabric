package de.leximon.quartz.api.event;

public interface Cancellable {

    default void cancel() {
        this.setCancelled(true);
    }

    void setCancelled(boolean cancel);

    boolean isCancelled();

}
