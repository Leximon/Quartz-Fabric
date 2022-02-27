package de.leximon.quartz.mixin.classes.block;

import de.leximon.quartz.api.block.ServersideBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    /**
     * Restrict block breaking to the block that the client would see
     */
    @Redirect(method = "processBlockBreakingAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private BlockState inject(ServerWorld instance, BlockPos blockPos) {
        BlockState blockState = instance.getBlockState(blockPos);
        if(blockState.getBlock() instanceof ServersideBlock qBlock)
            blockState = qBlock.getDisplayState(blockState);
        return blockState;
    }

}
