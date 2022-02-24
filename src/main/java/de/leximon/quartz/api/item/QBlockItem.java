package de.leximon.quartz.api.item;

import de.leximon.quartz.api.block.ServersideBlock;
import net.kyori.adventure.text.Component;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

public class QBlockItem extends BlockItem implements ServersideItem {

    private final Item item;
    private final Component name;

    public <B extends Block & ServersideBlock> QBlockItem(B block, Item item, Component name) {
        this(block, item, new Item.Settings()
                .group(item.getGroup())
                .maxCount(item.getMaxCount()), name);
    }

    public <B extends Block & ServersideBlock> QBlockItem(B block, Item item, Item.Settings settings, Component name) {
        super(block, settings);
        this.item = item;
        this.name = name;
    }

    @Override
    public Item getDisplayItem() {
        return item;
    }

    @Override
    public Component getDisplayName() {
        return name;
    }
}
