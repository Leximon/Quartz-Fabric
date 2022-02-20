package de.leximon.quartz.api.block;

import net.minecraft.block.BlockState;

public interface ServersideBlock {

    BlockState getDisplayState(BlockState originalState);
}
