package de.leximon.quartz.api.item;

import de.leximon.quartz.api.Quartz;
import net.kyori.adventure.text.Component;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;

public interface ServersideItem {

    Item getDisplayItem(ServerPlayerEntity player);

    Component getDisplayName(ServerPlayerEntity player);

    default void applyDisplayNbt(NbtCompound original, ServerPlayerEntity player) {}

    default void applyDisplayNbtWithId(NbtCompound original, ServerPlayerEntity player) {
        applyDisplayNbt(original, player);
        {
            NbtCompound display = original.getCompound("display");
            Component name = getDisplayName(player);
            boolean hasCustomName = display.contains("Name") || name == null;
            if(!hasCustomName)
                display.putString("Name", Text.Serializer.toJson(
                        Quartz.adventure().toNative(name).copy().styled(s -> !s.isItalic() ? s.withItalic(false) : s)
                ));
            display.putBoolean("hasCustomName", hasCustomName);
            original.put("display", display);
        }
        original.putString("customId", Registry.ITEM.getId((Item) this).toString());
    }

}
