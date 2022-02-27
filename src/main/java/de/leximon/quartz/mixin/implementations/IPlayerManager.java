package de.leximon.quartz.mixin.implementations;

import io.netty.channel.ChannelId;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;

public interface IPlayerManager {

    Map<ChannelId, ServerPlayerEntity> getPlayerChannelMap();

    default ServerPlayerEntity getPlayerByChannelId(ChannelId channelId) {
        return getPlayerChannelMap().get(channelId);
    }

}
