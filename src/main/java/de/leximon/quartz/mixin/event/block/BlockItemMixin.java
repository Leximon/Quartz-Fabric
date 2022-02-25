package de.leximon.quartz.mixin.event.block;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.block.BlockData;
import de.leximon.quartz.api.event.block.BlockPlaceEvent;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    /**
     * BlockPlaceEvent
     */
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void inject(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        ServerWorld world = (ServerWorld) context.getWorld();
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        BlockPos blockPos = context.getBlockPos();
        BlockPlaceEvent event = new BlockPlaceEvent(
                player, context,
                new BlockData(world, blockPos, state, null),
                new BlockData(world, blockPos, world.getBlockState(blockPos), world.getBlockEntity(blockPos))
        );
        Quartz.callEvent(event);
        if (event.isCancelled()) {
            if(player != null)
                player.currentScreenHandler.updateToClient();
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}
