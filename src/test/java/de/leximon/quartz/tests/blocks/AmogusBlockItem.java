package de.leximon.quartz.tests.blocks;

import de.leximon.quartz.api.block.ServersideBlock;
import de.leximon.quartz.api.item.QBlockItem;
import net.kyori.adventure.text.Component;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class AmogusBlockItem extends QBlockItem {
    public <B extends Block & ServersideBlock> AmogusBlockItem(B block) {
        super(block, Items.REDSTONE_BLOCK);
    }

    @Override
    public Item getDisplayItem(ServerPlayerEntity player) {

        return player.getEntityName().equalsIgnoreCase("Leximon") ? Items.TNT : Items.REDSTONE_BLOCK;
    }

    @Override
    public Component getDisplayName(ServerPlayerEntity player) {
        return Component.text("Suspicious Block " + player.getEntityName());
    }
}
