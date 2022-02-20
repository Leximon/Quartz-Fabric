package de.leximon.quartz.api;

import de.leximon.quartz.QuartzInitializer;
import lombok.experimental.UtilityClass;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@UtilityClass
public class Quartz {

    public static void registerBlock(Identifier id, Block block) {
        Registry.register(Registry.BLOCK, id, block);
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Identifier id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.create(factory, blocks).build(null));
    }

    public static void registerItem(Identifier id, Item item) {
        Registry.register(Registry.ITEM, id, item);
    }

    public static FabricServerAudiences adventure() {
        return QuartzInitializer.adventure();
    }

}
