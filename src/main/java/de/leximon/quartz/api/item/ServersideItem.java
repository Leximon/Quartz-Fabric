package de.leximon.quartz.api.item;

import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;

public interface ServersideItem {

    Item getDisplayItem();

    Text getDisplayName();

    default void applyDisplayNbt(NbtCompound original) {}



    default void applyDisplayNbtWithId(NbtCompound original) {
        applyDisplayNbt(original);
        {
            NbtCompound display = original.getCompound("display");
            Text name = getDisplayName();
            if(!display.contains("Name") && name != null)
                display.putString("Name", Text.Serializer.toJson(
                        name.copy().styled(s -> !s.isItalic() ? s.withItalic(false) : s)
                ));
            original.put("display", display);
        }
        original.putString("customId", Registry.ITEM.getId((Item) this).toString());
    }

}
