package org.epiphany.epiphany.player;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.epiphany.Epiphany;
import org.epiphany.api.Data;
import org.epiphany.utils.ChatUtils;

public class PlayerHandler {


  public static void flashAll() {
    GlobalNotifier.playSound(Sound.ITEM_TRIDENT_RETURN, 0.55f);
    GlobalNotifier.playSound(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.55f);

    Bukkit.getScheduler().runTaskLater(Epiphany.getInstance(), () -> {
      GlobalNotifier.sendTitle("ね", 30, 40, 50);
      GlobalNotifier.playSound("epiphany.sfx.flash", 1.25f);
    }, 20);
  }


  public static void kickAllPlayers() {
    for (Player each : Bukkit.getOnlinePlayers()) each.kick();
  }
  public static void kickAllPlayers(String reason) {
    for (Player each : Bukkit.getOnlinePlayers()) each.kickPlayer(ChatUtils.format(reason));
  }


  public static void addEffectToAll(PotionEffectType type, int duration, int amplifier) {
    for (Player each : Bukkit.getOnlinePlayers()) {
      each.addPotionEffect(new PotionEffect(type, duration, amplifier, true, false));
    }
  }
  public static void addEffectToAll(PotionEffectType type, int amplifier) {
    for (Player each : Bukkit.getOnlinePlayers()) {
      each.addPotionEffect(new PotionEffect(type, PotionEffect.INFINITE_DURATION, amplifier, true, false));
    }
  }


  public static void teleportAllToWorld(World world) {
    if (world == null) return;

    for (Player each : Bukkit.getOnlinePlayers()) each.teleport(world.getSpawnLocation());

  }


  public static void freezeAll(boolean action) {
    for (Player each : Bukkit.getOnlinePlayers()) {

      var isFreezed = Data.get(each, "freeze", PersistentDataType.BOOLEAN);
      if (isFreezed == null) return;

      var finalAction = action && !isFreezed;
      Data.set(each, "freeze", PersistentDataType.BOOLEAN, finalAction);

    }
  }
  public static void freeze(Player player, boolean action) {
    var isFreezed = Data.get(player, "freeze", PersistentDataType.BOOLEAN);
    if (isFreezed == null) return;


    var finalAction = action && !isFreezed;
    Data.set(player, "freeze", PersistentDataType.BOOLEAN, finalAction);
  }


}
