package de.leximon.quartz.mixin.entity;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.entity.PlayerUtil;
import de.leximon.quartz.mixin.miscellaneous.PlayerManagerAccessor;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements PlayerUtil {

    private ServerScoreboard scoreboard;

    @Override
    public void setScoreboard(ServerScoreboard scoreboard) {
        final MinecraftServer server = Quartz.getServer();
        final PlayerManager playerManager = server.getPlayerManager();
        ((PlayerManagerAccessor) playerManager).quartzSendScoreboard(scoreboard, (ServerPlayerEntity) (Object) this);
        this.scoreboard = scoreboard;
    }

    @Override
    public ServerScoreboard getScoreboard() {
        return scoreboard;
    }
}
