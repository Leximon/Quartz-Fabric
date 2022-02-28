package de.leximon.quartz.testing;

import de.leximon.quartz.QuartzInitializer;
import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.entity.PlayerUtil;
import de.leximon.quartz.api.event.EventHandler;
import de.leximon.quartz.api.event.block.BlockBreakEvent;
import de.leximon.quartz.api.event.block.BlockPlaceEvent;
import de.leximon.quartz.api.event.inventory.InventoryClickEvent;
import de.leximon.quartz.api.event.player.PlayerInteractAtEntityEvent;
import de.leximon.quartz.api.event.player.PlayerJoinEvent;
import de.leximon.quartz.api.event.player.PlayerLoginEvent;
import de.leximon.quartz.api.event.player.PlayerQuitEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class TestListener {

    private static HashMap<UUID, ServerScoreboard> scoreboards = new HashMap<>();

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent e) {
        ((PlayerUtil) e.getPlayer()).sendActionBar(Component.text("Amongus?"));
        ((PlayerUtil) e.getPlayer()).sendPlayerListFooter(Component.text("IMAGINE A PLACE; WHERE YOU DIE LOL"));
        ((PlayerUtil) e.getPlayer()).setDisplayScoreboard(Quartz.getMainScoreboard());
    }

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e)  {
        var p = (PlayerUtil) e.getPlayer();
        p.setDisplayScoreboard(Quartz.getMainScoreboard().equals(p.getDisplayScoreboard()) ? scoreboards.get(e.getPlayer().getUuid()) : Quartz.getMainScoreboard());
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
        System.out.println("amommma");
        var p = e.getPlayer();
        e.setJoinMessage(Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("+", TextColor.color(0x00ff00)))
                .append(Component.text("] ", NamedTextColor.DARK_GRAY))
                .append(Component.text(p.getEntityName(), NamedTextColor.GRAY).hoverEvent(HoverEvent.showText(Component.text("gawpoeuqpiwe"))))
        );

        Quartz.getScheduler().runTaskLater(task -> {
            var scoreboard = Quartz.createNewScoreboard();
            ((PlayerUtil) p).setDisplayScoreboard(scoreboard);
            ScoreboardObjective objective = scoreboard.addObjective("test", ScoreboardCriterion.DUMMY, Quartz.adventure().toNative(Component.text("amongas")), ScoreboardCriterion.RenderType.HEARTS);
            scoreboard.getPlayerScore(UUID.randomUUID().toString(), objective).setScore(new Random().nextInt(0, 10000));
            scoreboard.setObjectiveSlot(1, objective);

            Team team = scoreboard.addTeam("test");
            team.setColor(Formatting.RED);
            team.setPrefix(Quartz.adventure().toNative(
                    Component.text("Gakld | ", TextColor.color(0xff0ff0))
            ));
            scoreboard.addPlayerToTeam(p.getEntityName(), team);
            scoreboards.put(p.getUuid(), scoreboard);
        }, 20);
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
