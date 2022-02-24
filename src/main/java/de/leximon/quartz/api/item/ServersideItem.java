package de.leximon.quartz.api.item;

import de.leximon.quartz.api.Quartz;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;

public interface ServersideItem {

    Item getDisplayItem();

    Component getDisplayName();

    default void applyDisplayNbt(NbtCompound original) {}

    default void applyDisplayNbtWithId(NbtCompound original) {
        applyDisplayNbt(original);
        {
            NbtCompound display = original.getCompound("display");
            Component name = getDisplayName();
            if(!display.contains("Name") && name != null)
                display.putString("Name", Text.Serializer.toJson(
                        Quartz.adventure().toNative(name).copy().styled(s -> !s.isItalic() ? s.withItalic(false) : s)
                ));
            original.put("display", display);
        }
        original.putString("customId", Registry.ITEM.getId((Item) this).toString());
    }

}
