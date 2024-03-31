package org.motion.utils;

import net.minecraft.nbt.CompoundTag;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.motion.MotionCore;

import java.util.*;
import java.util.stream.Collectors;


public class ItemBuilder {
    protected ItemStack is;
    protected ItemMeta im;
    protected LeatherArmorMeta leather;

    public ItemBuilder() { this(Material.AIR); }
    public ItemBuilder(Material material) { this(material, 1); }
    public ItemBuilder(Material material, int amount) { this(new ItemStack(material, amount)); }
    public ItemBuilder(ItemStack itemStack) { this.is = itemStack; }


    public ItemBuilder setAmount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        this.im = this.is.getItemMeta();
        this.im.setCustomModelData(data);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setID(String id) {
        this.im = this.is.getItemMeta();
        this.im.getPersistentDataContainer().set(new NamespacedKey(MotionCore.getInstance(), "id"), PersistentDataType.STRING, id);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setPlayer(String id) {
        this.im = this.is.getItemMeta();
        this.im.getPersistentDataContainer().set(new NamespacedKey(MotionCore.getInstance(), "player"), PersistentDataType.STRING, id.toLowerCase());
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addNBTTag(String key, int value) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(this.is);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(key, value);

        this.is = CraftItemStack.asBukkitCopy(stack);
        return this;
    }

    public boolean hasID() {
        if (!this.is.hasItemMeta() || this.is.getItemMeta() == null) return false;

        return this.is.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(MotionCore.getInstance(), "id"), PersistentDataType.STRING);
    }
    public String getID() {
        return hasID() ? this.is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MotionCore.getInstance(), "id"), PersistentDataType.STRING) : ChatColor.stripColor(is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() : is.getType().name().toLowerCase().replace(" ", "_"));
    }

    public ItemBuilder setUnbreakable(boolean b) {
        this.im = this.is.getItemMeta();
        this.im.setUnbreakable(b);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        this.im = this.is.getItemMeta();
        this.im.setDisplayName(ChatUtils.format(name));
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        this.im = this.is.getItemMeta();

        if (this.im instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) this.im).addStoredEnchant(enchantment, level, true);
        } else {
            this.im.addEnchant(enchantment, level, true);
        }

        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder cannotRepair() {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(is);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean("", true);
        tag.putBoolean("", true);

        return this;
    }

    public ItemBuilder setGlint(boolean glint) {
        if (glint) {
            this.im = this.is.getItemMeta();
            this.im.addEnchant(Enchantment.DURABILITY,1,true);
            this.im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            this.is.setItemMeta(this.im);
            return this;
        }
        else {
            this.im = this.is.getItemMeta();
            for (Enchantment enchantment : Enchantment.values()) this.im.removeEnchant(enchantment);
            this.is.setItemMeta(this.im);
            return this;
        }
    }

    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchantments) {
        this.im = this.is.getItemMeta();
        if (!enchantments.isEmpty())
            for (Enchantment ench : enchantments.keySet())
                this.im.addEnchant(ench, enchantments.get(ench), true);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... itemflag) {
        this.im = this.is.getItemMeta();
        this.im.addItemFlags(itemflag);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.im = this.is.getItemMeta();
        this.im.setLore(lore);
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addLore(String value) {
        List<String> lore = new ArrayList<>();
        this.im = this.is.getItemMeta();

        lore.add(ChatUtils.format(value));
        if (this.im.hasLore()) lore.addAll(this.im.getLore());

        this.im.setLore(lore);
        this.is.setItemMeta(this.im);

        return this;
    }


    public ItemBuilder setOwner(String name) {

        SkullMeta meta = (SkullMeta) is.getItemMeta();
        meta.setOwner(name);
        this.is.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setLore(String... lore) {
        this.im = this.is.getItemMeta();

        this.im.setLore(Arrays.stream(lore).map(ChatUtils::format).collect(Collectors.toList()));
        this.is.setItemMeta(this.im);
        return this;
    }

    public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier) {

        this.im = this.is.getItemMeta();
        this.im.addAttributeModifier(attribute, attributeModifier);
        this.is.setItemMeta(this.im);
        return this;

    }

    public <T> ItemBuilder addNBT(String key, T value) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(is);
        CompoundTag tag = stack.getOrCreateTag();
        if (value instanceof Integer) tag.putInt(key, (Integer) value);
        stack.setTag(tag);
        is = stack.asBukkitCopy();
        return this;
    }

    public ItemBuilder addAttributeModifier (Attribute attribute, double value, AttributeModifier.Operation operation, EquipmentSlot slot) {

        this.im = this.is.getItemMeta();
        this.im.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.getKey().getNamespace(), value, operation, slot));
        this.is.setItemMeta(this.im);
        return this;

    }
    public ItemBuilder setLeatherColor(int red,int green,int blue){
        this.leather = (LeatherArmorMeta) this.is.getItemMeta();
        this.leather.setColor(Color.fromRGB(red,green,blue));
        this.is.setItemMeta(this.leather);
        return this;
    }


    public static ItemBuilder stackToBuilder(ItemStack stack) {
        return new ItemBuilder(stack);
    }

    public static boolean equals(String id, ItemStack stack) {
        return ItemBuilder.stackToBuilder(stack).hasID() && ItemBuilder.stackToBuilder(stack).getID().equalsIgnoreCase(id);
    }

    public ItemStack build() { return this.is; }
}