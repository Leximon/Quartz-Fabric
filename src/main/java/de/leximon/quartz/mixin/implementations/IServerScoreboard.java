package de.leximon.quartz.mixin.implementations;

public interface IServerScoreboard {

    int getLineId();

    void setLineId(int id);

    default void increaseLineId() {
        setLineId(getLineId() + 1);
    }

}
