package de.leximon.quartz.mixin.classes.block;

import de.leximon.quartz.api.block.QBlock;
import de.leximon.quartz.api.block.ServersideBlock;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkDeltaUpdateS2CPacket.class)
public class ChunkDeltaUpdateS2CPacketMixin {

    /**
     * replace custom blocks
     */
    @Redirect(method = "<init>(Lnet/minecraft/util/math/ChunkSectionPos;Lit/unimi/dsi/fastutil/shorts/ShortSet;Lnet/minecraft/world/chunk/ChunkSection;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkSection;getBlockState(III)Lnet/minecraft/block/BlockState;"))
    private BlockState inject(ChunkSection instance, int x, int y, int z) {
        BlockState blockState = instance.getBlockState(x, y, z);
        if(blockState.getBlock() instanceof ServersideBlock qBlock) {
            return qBlock.getDisplayState(blockState);
        }
        return blockState;
    }

}
