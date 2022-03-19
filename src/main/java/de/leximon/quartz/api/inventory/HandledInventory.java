package de.leximon.quartz.api.inventory;

import de.leximon.quartz.api.Quartz;
import de.leximon.quartz.api.event.EventHandler;
import de.leximon.quartz.api.event.inventory.InventoryClickEvent;
import de.leximon.quartz.api.event.inventory.InventoryCloseEvent;
import de.leximon.quartz.api.event.inventory.InventoryOpenEvent;
import net.kyori.adventure.text.Component;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.HashSet;

public abstract class HandledInventory implements Inventory, NamedScreenHandlerFactory {

    private final DefaultedList<ItemStack> inventory;
    private final HashSet<ServerPlayerEntity> viewers = new HashSet<>();

    public HandledInventory(int size) {
        inventory = DefaultedList.ofSize(size, ItemStack.EMPTY);
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(inventory, slot, amount);
        if (!itemStack.isEmpty())
            this.markDirty();

        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        stack = stack.copy();
        inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        this.markDirty();
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public Text getDisplayName() {
        return Quartz.adventure().toNative(getTitle());
    }

    public abstract Component getTitle();

    @Override
    public void clear() {
        inventory.clear();
    }

    public DefaultedList<ItemStack> getInvStackList() {
        return inventory;
    }

    @Override
    public final void onOpen(PlayerEntity player) { Inventory.super.onOpen(player); }

    @Override
    public final void onClose(PlayerEntity player) { Inventory.super.onClose(player); }

    // events
    public void onOpen(InventoryOpenEvent e) {}
    public void onClose(InventoryCloseEvent e) {}
    public void onClick(InventoryClickEvent e) {}

    // handle inventory
    public static HashMap<ScreenHandler, HandledInventory> HANDLED_INVENTORIES = new HashMap<>();

    public static void registerForEvents(ServerPlayerEntity player, ScreenHandler screenHandler, HandledInventory inv) {
        inv.viewers.add(player);
        HANDLED_INVENTORIES.put(screenHandler, inv);

        inv.onOpen(new InventoryOpenEvent(player, screenHandler));
    }

    @EventHandler
    public static void onInventoryClose(InventoryCloseEvent e) {
        ScreenHandler screenHandler = e.getScreenHandler();
        HandledInventory inv = HANDLED_INVENTORIES.remove(screenHandler);

        inv.viewers.remove(e.getPlayer());
        inv.onClose(e);
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e) {
        ScreenHandler screenHandler = e.getScreenHandler();
        HandledInventory inv = HANDLED_INVENTORIES.get(screenHandler);
        if(inv == null)
            return;

        inv.onClick(e);
    }
}
