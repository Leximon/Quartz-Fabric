package de.leximon.quartz.mixin.implementations;

import net.minecraft.server.network.ServerPlayerEntity;

public interface IPacketByteBuf {

    void setPlayer(ServerPlayerEntity player);

    ServerPlayerEntity getPlayer();

}
