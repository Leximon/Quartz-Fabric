package de.leximon.quartz.api.entity;

import de.leximon.quartz.api.inventory.ScreenHandlerFactory;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.screen.ScreenHandler;

public interface PlayerUtil extends ForwardingAudience.Single {

    void setDisplayScoreboard(ServerScoreboard scoreboard);

    ServerScoreboard getDisplayScoreboard();

    <I extends ScreenHandler> I openInventory(Component title, ScreenHandlerFactory<I> factory);

}
