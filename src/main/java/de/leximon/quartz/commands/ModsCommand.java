package de.leximon.quartz.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import de.leximon.quartz.api.Quartz;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;
import java.util.function.Function;

public class ModsCommand {

    public static void init(CommandDispatcher<ServerCommandSource> d) {
        d.register(CommandManager.literal("mods")
                .executes(context -> listMods(context, false))
                .then(CommandManager.literal("all")
                        .executes(context -> listMods(context, true))
                )
        );
    }

    private static int listMods(CommandContext<ServerCommandSource> context, boolean all) {
        Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
        if(!all)
            mods.removeIf(m -> {
                String name = m.getMetadata().getName();
                return name.startsWith("fabric-") || name.startsWith("java") || name.startsWith("minecraft");
            });
        var b = Component.text()
                .append(Component.text("Mods ("))
                .append(Component.text(mods.size()))
                .append(Component.text("): "));
        int i = 0;
        for (ModContainer mod : mods) {
            ModMetadata metadata = mod.getMetadata();

            var metadataText = Component.text();
            metadataText.append(Component.text("ID: ", NamedTextColor.WHITE))
                    .append(Component.text(metadata.getId(), NamedTextColor.BLUE))
                    .append(Component.newline());
            metadataText.append(Component.text("Version: ", NamedTextColor.WHITE))
                    .append(Component.text(metadata.getVersion().getFriendlyString(), NamedTextColor.BLUE))
                    .append(Component.newline());

            var authors = metadata.getAuthors();
            if(authors.size() > 0) {
                metadataText.append(Component.text(authors.size() == 1 ? "Author: " : "Authors: ", NamedTextColor.WHITE));
                listed(metadataText, authors, author -> Component.text(author.getName(), NamedTextColor.BLUE));
                metadataText.append(Component.newline());
            }

            var license = metadata.getLicense();
            if (license.size() > 0) {
                metadataText.append(Component.text("License: ", NamedTextColor.WHITE));
                listed(metadataText, license, ls -> Component.text(ls, NamedTextColor.BLUE));
                metadataText.append(Component.newline());
            }

            var dependencies = metadata.getDependencies();
            if(dependencies.size() > 0) {
                metadataText.append(Component.text("Dependencies: ", NamedTextColor.WHITE));
                listed(metadataText, dependencies, dep -> Component.text(dep.getModId(), NamedTextColor.BLUE));
                metadataText.append(Component.newline());
            }

            if(metadata.getDescription() != null) {
                metadataText.append(Component.newline());
                metadataText.append(Component.text(metadata.getDescription(), NamedTextColor.WHITE));
            }
            i++;
            if(i != mods.size())
                b.append(Component.text(metadata.getName(), i % 2 == 0 ? TextColor.color(0x80BEF5) : TextColor.color(0x4296f5)).hoverEvent(HoverEvent.showText(metadataText.build())))
                        .append(Component.text(", ", NamedTextColor.WHITE));
        }
        context.getSource().sendFeedback(Quartz.adventure().toNative(b.build()), false);
        return 1;
    }

    private static <T> void listed(TextComponent.Builder builder, Collection<T> list, Function<T, Component> converter) {
        int j = 0;
        for (T v : list) {
            builder.append(converter.apply(v));
            j++;
            if (j != list.size())
                builder.append(Component.text(", ", NamedTextColor.WHITE));
        }
    }

}
