package de.leximon.quartz.test;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.item.QBlockItem;

import de.leximon.quartz.test.blocks.AmogusBlock;
import de.leximon.quartz.test.blocks.AmogusBlockItem;
import de.leximon.quartz.test.blocks.ExampleBlock;
import de.leximon.quartz.test.blocks.ExampleBlockEntity;
import de.leximon.quartz.test.listeners.TestListener;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.kyori.adventure.text.Component;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class TestInitializer implements DedicatedServerModInitializer {

    public static final AmogusBlock AMOGUS_BLOCK = new AmogusBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK));
    public static final ExampleBlock FLAMETHROWER = new ExampleBlock(FabricBlockSettings.copyOf(Blocks.DISPENSER));
    public static BlockEntityType<ExampleBlockEntity> EXAMPLE_BLOCK_ENTITY;

    @Override
    public void onInitializeServer() {
        Quartz.registerBlock(id("amogus"), AMOGUS_BLOCK);
        Quartz.registerBlock(id("flamethrower"), FLAMETHROWER);
        EXAMPLE_BLOCK_ENTITY = Quartz.registerBlockEntity(id("flamethrower"), ExampleBlockEntity::new, FLAMETHROWER);

        Quartz.registerItem(id("flamethrower"), new QBlockItem(FLAMETHROWER, Items.OBSERVER, Component.text("Flamethrower")));
        Quartz.registerItem(id("amogus"), new AmogusBlockItem(AMOGUS_BLOCK));

        Quartz.registerEvents(TestListener.class);
    }

    public static Identifier id(String name) {
        return new Identifier("quartz_tests", name);
    }
}
