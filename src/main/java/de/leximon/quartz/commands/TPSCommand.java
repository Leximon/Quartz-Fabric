package de.leximon.quartz.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.EventHandler;
import de.leximon.quartz.api.event.server.ServerStartedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TPSCommand {

    public static void init(CommandDispatcher<ServerCommandSource> d) {
        d.register(CommandManager.literal("tps")
                .executes(TPSCommand::tps)
        );
    }

    private static int tps(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(Quartz.adventure().toNative(
                Component.text("Ticks Per Second: ", TextColor.color(0x4296f5))
                        .append(Component.text(tps, TextColor.color(HSVLike.of(0.333f * (tps / 20f), 1, 1))))
        ), false);
        return 1;
    }

    public static int lastTicks = 0, tps = 20;

    @EventHandler
    public static void onServerStarted(ServerStartedEvent e) {
        MinecraftServer server = e.getServer();
        new Thread(() -> {
            while(true) {
                int ticks = server.getTicks();
                tps = ticks - lastTicks;
                lastTicks = ticks;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

}
