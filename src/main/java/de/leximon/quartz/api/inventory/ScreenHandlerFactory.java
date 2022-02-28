package de.leximon.quartz.api.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

@FunctionalInterface
public interface ScreenHandlerFactory<I extends ScreenHandler> {

    I createMenu(int syncId, PlayerInventory inv, PlayerEntity player);

}
