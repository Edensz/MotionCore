package org.epiphany.api;

import org.epiphany.Epiphany;
import org.epiphany.utils.ChatUtils;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.epiphany.utils.GlobalUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ItemBuilder {

  protected ItemStack is;
  protected ItemMeta im;
  protected LeatherArmorMeta leather;
  private final List<PotionEffect> potionList = new ArrayList<>();
  public ItemBuilder() {
    this(Material.AIR);
  }
  public ItemBuilder(Material material) {
    this(material, 1);
  }
  public ItemBuilder(Material material, int amount) {
    this(new ItemStack(material, amount));
  }
  public ItemBuilder(ItemStack itemStack) {
    this.is = itemStack;
  }
  public ItemBuilder setAmount(int amount) {
    this.is.setAmount(amount);
    return this;
  }
  public ItemBuilder addEffect(PotionEffectType type, int effectDuration, int effectAmplifier) {
    var effect = new PotionEffect(type, effectDuration, effectAmplifier);
    this.potionList.add(effect); //??'
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
    this.im.getPersistentDataContainer().set(new NamespacedKey(Epiphany.getInstance(), "id"), PersistentDataType.STRING, id.toLowerCase());
    this.is.setItemMeta(this.im);
    return this;
  }

  public ItemBuilder setPlayer(String id) {
    this.im = this.is.getItemMeta();
    this.im.getPersistentDataContainer().set(new NamespacedKey(Epiphany.getInstance(), "player"), PersistentDataType.STRING, id.toLowerCase());
    this.is.setItemMeta(this.im);
    return this;
  }

  public String getPlayer() {
    return hasID() ? this.is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Epiphany.getInstance(), "player"), PersistentDataType.STRING) : ChatColor.stripColor(is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() : is.getType().name().toLowerCase().replace(" ", "_"));
  }

  public ItemBuilder addNBTTag(String key, int value) {
    net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(this.is);
    CompoundTag tag = stack.getOrCreateTag();
    tag.putInt(key, value);

    this.is = CraftItemStack.asBukkitCopy(stack);
    return this;
  }

  public ItemBuilder setArmorID(String id) {
    this.im = this.is.getItemMeta();

    this.im.getPersistentDataContainer().set(new NamespacedKey(Epiphany.getInstance(), "armor_id"), PersistentDataType.STRING, id.toLowerCase());
    this.is.setItemMeta(this.im);
    return this;
  }



  public ItemBuilder perk() {
    this.im = this.is.getItemMeta();
    this.im.getPersistentDataContainer().set(new NamespacedKey(Epiphany.getInstance(), "perk_id"), PersistentDataType.BYTE, (byte) 1);
    this.is.setItemMeta(this.im);
    return this;
  }

  public List<PotionEffect> getPotionEffects() {
    return new ArrayList<>(this.potionList);
  }
  public boolean hasID() {
    if(!this.is.hasItemMeta() || this.is.getItemMeta() == null)return false;
    return this.is.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Epiphany.getInstance(), "id"), PersistentDataType.STRING);
  }

  public boolean hasPID() {
    if(!this.is.hasItemMeta() || this.is.getItemMeta() == null)return false;
    return this.is.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Epiphany.getInstance(), "perk_id"), PersistentDataType.BYTE);
  }

  public String getID() {
    return hasID() ? this.is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Epiphany.getInstance(), "id"), PersistentDataType.STRING) : ChatColor.stripColor(is.getItemMeta().hasDisplayName() ? is.getItemMeta().getDisplayName() : is.getType().name().toLowerCase().replace(" ", "_"));
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

  public ItemBuilder setGlint() {
    this.im = this.is.getItemMeta();
    this.im.addEnchant(Enchantment.DURABILITY,1,true);
    this.im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    this.is.setItemMeta(this.im);
    return this;
  }

  public ItemBuilder removeGlint() {
    this.im = this.is.getItemMeta();
    for (Enchantment enchantment : Enchantment.values()) this.im.removeEnchant(enchantment);
    this.is.setItemMeta(this.im);
    return this;
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

  public ItemBuilder addUnplaceableTag(){

    this.im = is.getItemMeta();
    this.im.getPersistentDataContainer().set(Data.Key("can_place"), PersistentDataType.STRING, "false");
    this.is.setItemMeta(im);
    return this;
  }

  public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier) {

    this.im = this.is.getItemMeta();
    this.im.addAttributeModifier(attribute, attributeModifier);
    this.is.setItemMeta(this.im);
    return this;

  }

  public boolean hasData(ItemStack s, String id, PersistentDataType type) {
    this.im = this.is.getItemMeta();
    boolean h = im.getPersistentDataContainer().has(Data.Key(id), type);
    this.is.setItemMeta(this.im);

    return h;
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


  public ItemBuilder setCharm() {
    this.im = this.is.getItemMeta();
    this.im.getPersistentDataContainer().set(new NamespacedKey(Epiphany.getInstance(), "charm"), PersistentDataType.BYTE, (byte) 1);
    this.is.setItemMeta(this.im);
    return this;
  }

  public boolean isCharm() {
    if(!this.is.hasItemMeta() || this.is.getItemMeta() == null) return false;
    return this.is.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Epiphany.getInstance(), "charm"), PersistentDataType.BYTE);
  }


  public ItemBuilder setPetItem(String id){
    this.im = this.is.getItemMeta();
    this.im.getPersistentDataContainer().set(new NamespacedKey(Epiphany.getInstance(), "pet_id"), PersistentDataType.STRING, id);
    this.im.getPersistentDataContainer().set(Data.Key("can_place"), PersistentDataType.STRING, "false");
    this.im.getPersistentDataContainer().set(Data.Key("pet_level"), PersistentDataType.INTEGER,1);
    this.im.getPersistentDataContainer().set(Data.Key("pet_xp"), PersistentDataType.INTEGER,0);
    this.im.getPersistentDataContainer().set(Data.Key("pet_max_xp"), PersistentDataType.INTEGER,500);
    this.is.setItemMeta(this.im);
    return this;
  }


  public static ItemBuilder stackToBuilder(ItemStack stack) {
    return new ItemBuilder(stack);
  }

  public static boolean equals(String id, ItemStack stack) {
    return ItemBuilder.stackToBuilder(stack).hasID() && ItemBuilder.stackToBuilder(stack).getID().equalsIgnoreCase(id);
  }
  public ItemStack build() {
    return this.is;
  }
}
