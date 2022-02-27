package de.leximon.quartz.mixin.classes.block;

import de.leximon.quartz.api.block.ServersideBlock;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockUpdateS2CPacket.class)
public class BlockUpdateS2CPacketMixin {

    @Mutable @Shadow @Final private BlockState state;

    /**
     * replace custom block if needed
     */
    @Redirect(method = "<init>(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/network/packet/s2c/play/BlockUpdateS2CPacket;state:Lnet/minecraft/block/BlockState;", opcode = Opcodes.PUTFIELD))
    private void inject(BlockUpdateS2CPacket instance, BlockState value) {
        if(value.getBlock() instanceof ServersideBlock qBlock) {
            this.state = qBlock.getDisplayState(value);
            return;
        }
        this.state = value;
    }
}
