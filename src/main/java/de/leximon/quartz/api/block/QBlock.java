package de.leximon.quartz.api.block;

import net.minecraft.block.Block;

public abstract class QBlock extends Block implements ServersideBlock {

    public QBlock(Settings settings) {
        super(settings);
    }

}
