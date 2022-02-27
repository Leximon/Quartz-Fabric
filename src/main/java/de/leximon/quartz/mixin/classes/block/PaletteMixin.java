package de.leximon.quartz.mixin.classes.block;

import de.leximon.quartz.api.block.ServersideBlock;
import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.ArrayPalette;
import net.minecraft.world.chunk.BiMapPalette;
import net.minecraft.world.chunk.Palette;
import net.minecraft.world.chunk.SingularPalette;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({ArrayPalette.class, BiMapPalette.class, SingularPalette.class})
public abstract class PaletteMixin<T> implements Palette<T> {

    /**
     * replace custom blocks
     */
    @SuppressWarnings("unchecked")
    @ModifyArg(method = "writePacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/IndexedIterable;getRawId(Ljava/lang/Object;)I"), index = 0)
    private T inject(T originalValue) {
        if(originalValue instanceof BlockState state && state.getBlock() instanceof ServersideBlock qBlock) {
            return (T) qBlock.getDisplayState(state);
        }
        return originalValue;
    }

}
