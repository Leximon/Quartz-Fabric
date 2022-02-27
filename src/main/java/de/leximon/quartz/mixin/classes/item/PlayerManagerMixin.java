package de.leximon.quartz.mixin.classes.item;

import com.google.common.collect.Maps;
import de.leximon.quartz.mixin.implementations.IPlayerManager;
import io.netty.channel.ChannelId;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin implements IPlayerManager {

    private final Map<ChannelId, ServerPlayerEntity> playerChannelMap = Maps.newHashMap();

    @Inject(method = "onPlayerConnect", at = @At("HEAD"))
    private void injectAdd(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        playerChannelMap.put(((ClientConnectionAccessor) connection).getChannel().id(), player);
    }

    @Inject(method = "remove", at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0))
    private void injectRemove(ServerPlayerEntity player, CallbackInfo ci) {
        playerChannelMap.remove(((ClientConnectionAccessor) player.networkHandler.connection).getChannel().id());
    }

    @Override
    public Map<ChannelId, ServerPlayerEntity> getPlayerChannelMap() {
        return playerChannelMap;
    }
}
