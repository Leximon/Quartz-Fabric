package de.leximon.quartz.mixin.classes.block;

import de.leximon.quartz.api.block.BlockAssociatedEntityContainer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldChunk.class)
public class WorldChunkMixin {

    @Inject(method = "removeBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;removeGameEventListener(Lnet/minecraft/block/entity/BlockEntity;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void injectRemove(BlockPos pos, CallbackInfo ci, BlockEntity blockEntity) {
        if (blockEntity instanceof BlockAssociatedEntityContainer entityContainer)
            entityContainer.getAssociatedEntityContainer().killAll();
    }

    @Inject(method = "updateTicker", at = @At("HEAD"))
    private <T extends BlockEntity> void injectUpdate(T blockEntity, CallbackInfo ci) {
        if(blockEntity instanceof BlockAssociatedEntityContainer entityContainer)
            entityContainer.getAssociatedEntityContainer().spawnAll();
    }

}
