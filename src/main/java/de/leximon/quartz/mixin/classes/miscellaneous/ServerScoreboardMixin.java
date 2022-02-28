package de.leximon.quartz.mixin.classes.miscellaneous;

import de.leximon.quartz.api.entity.PlayerUtil;
import net.minecraft.network.Packet;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;

/**
 * if the player has its own scoreboard only send updates to him
 */
@Mixin(ServerScoreboard.class)
public class ServerScoreboardMixin {

    @Shadow @Final private MinecraftServer server;

    @Redirect(
            method = {
                    "updateScore", "updateScoreboardTeam", "updateScoreboardTeamAndPlayers", "updateRemovedTeam",
                    "updatePlayerScore(Ljava/lang/String;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
                    "updatePlayerScore(Ljava/lang/String;)V", "updateExistingObjective", "setObjectiveSlot"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/Packet;)V")
    )
    private void inject(PlayerManager instance, Packet<?> packet) {
        if(this.equals(server.getScoreboard())) {
            instance.sendToAll(packet);
            return;
        }
        for (ServerPlayerEntity player : instance.getPlayerList())
            if (this.equals(((PlayerUtil) player).getDisplayScoreboard()))
                player.networkHandler.sendPacket(packet);
    }

    @Redirect(
            method = {"addScoreboardObjective", "removeScoreboardObjective"},
            at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;", ordinal = 0)
    )
    private Iterator<ServerPlayerEntity> inject(List<ServerPlayerEntity> instance) {
        if (this.equals(server.getScoreboard()))
            return instance.iterator();
        return instance.stream()
                .filter(p -> this.equals(((PlayerUtil) p).getDisplayScoreboard()))
                .iterator();
    }

}
