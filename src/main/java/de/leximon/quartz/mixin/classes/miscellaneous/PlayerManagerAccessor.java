package de.leximon.quartz.mixin.classes.miscellaneous;

import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerManager.class)
public interface PlayerManagerAccessor {

    @Invoker("sendScoreboard")
    void quartzSendScoreboard(ServerScoreboard scoreboard, ServerPlayerEntity player);

}
