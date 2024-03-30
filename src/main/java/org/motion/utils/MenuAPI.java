package org.motion.utils;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.motion.MotionCore;

import javax.annotation.Nullable;


@Getter
public abstract class MenuAPI implements Listener, Cloneable {
    public enum PanelMode {MOVIE, VIEW, CHAIN}

    protected final Inventory inventory;
    protected final Player player;
    protected final String name;
    protected final PanelMode panelMode;

    public MenuAPI(@Nullable InventoryHolder holder, int size, String name, Player player, PanelMode panelMode) {
        this.inventory = Bukkit.createInventory(holder, size, ChatUtils.format(name));
        this.name = ChatUtils.format(name);
        this.player = player;
        this.panelMode = panelMode;

        Bukkit.getServer().getPluginManager().registerEvents(this, MotionCore.getInstance());
        initInventory();
    }


    public void empty() {
        for (int i = 0; i < inventory.getSize(); i++) {
            setItem(i, new ItemStack(Material.AIR));
        }
    }


    public void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }


    public void setRow(ItemStack item, int from, int to) {
        for (int i = from; i <= to; ++i) {
            inventory.setItem(i, item);
        }
    }


    public void setItems(ItemStack item, int... slots) {
        for (int i = 0; i <= slots.length-1; ++i) {
            inventory.setItem(slots[i], item);
        }
    }


    public void open() {
        player.closeInventory();
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN,1,0.75f);
    }


    public boolean isFull() {
        var spaces = 0;

        for (int i = 0; i < inventory.getSize(); i++) if (inventory.getItem(i) == null) spaces = spaces + 1;

        return spaces == 0;
    }


    public void setOutline(ItemStack itemStack) {
        setRow(itemStack, 0, 8);
        setRow(itemStack, 45, 53);
        setItems(itemStack, 9, 17, 18, 26, 27, 35, 36, 44);
    }



    public abstract void initInventory();

    @EventHandler
    public abstract void click(InventoryClickEvent event);

    @Override
    public MenuAPI clone() {
        try {return (MenuAPI) super.clone();}
        catch (CloneNotSupportedException e) {throw new AssertionError();}
    }


}