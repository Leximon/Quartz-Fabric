package de.leximon.quartz.mixin.item;

import de.leximon.quartz.api.item.ServersideItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static de.leximon.quartz.QuartzInitializer.LOGGER;

@Mixin(PacketByteBuf.class)
public abstract class PacketByteBufMixin {

    @Shadow public abstract ByteBuf writeBoolean(boolean value);

    @Shadow public abstract PacketByteBuf writeVarInt(int value);

    @Shadow public abstract ByteBuf writeByte(int value);

    @Shadow public abstract PacketByteBuf writeNbt(@Nullable NbtCompound compound);

    @Shadow public abstract boolean readBoolean();

    @Shadow public abstract int readVarInt();

    @Shadow public abstract byte readByte();

    @Shadow public abstract @Nullable NbtCompound readNbt();

    /**
     * @author Leximon
     * @reason send a custom item
     */
    @Overwrite
    public PacketByteBuf writeItemStack(ItemStack stack) {
        if (stack.isEmpty()) {
            this.writeBoolean(false);
        } else {

            Item item = stack.getItem();
            NbtCompound nbtCompound = null;
            if (item.isDamageable() || item.isNbtSynced())
                nbtCompound = stack.getNbt();

            if (item instanceof ServersideItem qItem) {
                if(nbtCompound == null)
                    nbtCompound = new NbtCompound();
                item = qItem.getDisplayItem();
                qItem.applyDisplayNbtWithId(nbtCompound);
            }
            this.writeBoolean(true);
            this.writeVarInt(Item.getRawId(item));
            this.writeByte(stack.getCount());
            this.writeNbt(nbtCompound);
        }
        return (PacketByteBuf) (Object) this;
    }

    /**
     * @author Leximon
     * @reason retrieve custom item
     */
    @Overwrite
    public ItemStack readItemStack() {
        if (!this.readBoolean()) {
            return ItemStack.EMPTY;
        }
        int id = this.readVarInt();
        byte count = this.readByte();
        NbtCompound nbt = this.readNbt();

        if(nbt != null && nbt.contains("customId", NbtCompound.STRING_TYPE)) {
            String customIdRaw = nbt.getString("customId");
            try {
                Identifier customId = new Identifier(customIdRaw);
                Item item = Registry.ITEM.get(customId);
                if (Item.getRawId(item) == 0) {
                    LOGGER.warn("Unknown custom id: {}", customIdRaw);
                } else {
                    id = Item.getRawId(item);
                    nbt.remove("customId");
                }
            } catch (InvalidIdentifierException e) {
                LOGGER.warn("Invalid custom id: {}", customIdRaw);
            }
        }
        ItemStack itemStack = new ItemStack(Item.byRawId(id), count);
        itemStack.setNbt(nbt);
        return itemStack;
    }

}
