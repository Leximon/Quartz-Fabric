package de.leximon.quartz.mixin.classes.item;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.mixin.implementations.IPacketByteBuf;
import de.leximon.quartz.mixin.implementations.IPlayerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketEncoder;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PacketEncoder.class)
public class PacketEncoderMixin {

    /**
     * Set the player in the PacketByteBuf that is being sent to
     */
    @Inject(method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;Lio/netty/buffer/ByteBuf;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;write(Lnet/minecraft/network/PacketByteBuf;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void inject(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf, CallbackInfo ci, NetworkState networkState, Integer integer, PacketByteBuf packetByteBuf, int i) {
        ServerPlayerEntity player = ((IPlayerManager) Quartz.getServer().getPlayerManager())
                .getPlayerByChannelId(channelHandlerContext.channel().id());
        ((IPacketByteBuf) packetByteBuf).setPlayer(player);

    }

}
