package de.leximon.quartz.api.entity;

import net.kyori.adventure.audience.ForwardingAudience;
import net.minecraft.scoreboard.ServerScoreboard;

public interface PlayerUtil extends ForwardingAudience.Single {

    void setScoreboard(ServerScoreboard scoreboard);

    ServerScoreboard getScoreboard();

}
