package de.leximon.quartz.testing;

import de.leximon.quartz.QuartzInitializer;
import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.EventHandler;
import de.leximon.quartz.api.event.block.BlockBreakEvent;
import de.leximon.quartz.api.event.block.BlockPlaceEvent;
import de.leximon.quartz.api.event.inventory.InventoryClickEvent;
import de.leximon.quartz.api.event.player.PlayerInteractAtEntityEvent;
import de.leximon.quartz.api.event.player.PlayerInteractEntityEvent;
import de.leximon.quartz.api.event.player.PlayerJoinEvent;
import de.leximon.quartz.api.event.player.PlayerLoginEvent;
import de.leximon.quartz.api.event.player.PlayerQuitEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;

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

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        System.out.println("eoiwqeoiuwqe");
        var p = e.getPlayer();
        e.setJoinMessage(Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("+", TextColor.color(0x00ff00)))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(p.getEntityName(), NamedTextColor.GRAY).hoverEvent(HoverEvent.showText(Component.text("gawpoeuqpiwe"))))
        );
    }

    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent e) {
        System.out.println("qweqwe");
        var p = e.getPlayer();
        e.setQuitMessage(Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("-", TextColor.color(0xff0000)))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(p.getEntityName(), NamedTextColor.GRAY).hoverEvent(HoverEvent.showText(Component.text("gawpoeuqpiwe"))))
        );

    }

    @EventHandler
    public static void onPlayerLogin(PlayerLoginEvent e) {
//        e.disallow(Component.text("Hamburger sucks", TextColor.color(0xB0FF6F)));
    }

}
