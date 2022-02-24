package de.leximon.quartz.testing;

import de.leximon.quartz.QuartzInitializer;
import de.leximon.quartz.api.event.EventHandler;
import de.leximon.quartz.api.event.block.BlockBreakEvent;
import de.leximon.quartz.api.event.block.BlockPlaceEvent;
import de.leximon.quartz.api.event.inventory.InventoryClickEvent;
import de.leximon.quartz.api.event.player.PlayerInteractAtEntityEvent;
import de.leximon.quartz.api.event.player.PlayerInteractEntityEvent;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;

public class TestListener {

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent e) {

    }

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e)  {

    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e) {

    }

    @EventHandler
    public static void onEntityInteract(PlayerInteractAtEntityEvent e) {
        QuartzInitializer.LOGGER.info("\nEntity: {}\nPos: {}", e.getEntity(), e.getPos());
//        e.cancel();
    }

}
