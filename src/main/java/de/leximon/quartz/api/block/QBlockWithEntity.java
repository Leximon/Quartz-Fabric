package de.leximon.quartz.api.block;

import net.minecraft.block.BlockWithEntity;

public abstract class QBlockWithEntity extends BlockWithEntity implements ServersideBlock {

    public QBlockWithEntity(Settings settings) {
        super(settings);
    }

}
