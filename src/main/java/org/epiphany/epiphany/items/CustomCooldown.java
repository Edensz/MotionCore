package org.epiphany.epiphany.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.epiphany.Epiphany;
import org.epiphany.api.Data;
import org.epiphany.api.ItemBuilder;
import org.epiphany.api.Registrable;
import org.epiphany.utils.ChatUtils;


@Registrable
public class CustomCooldown implements Listener {


  @EventHandler
  private void detect(PlayerQuitEvent event) {
    var player = event.getPlayer();
    var inventory = player.getInventory();

    for (int i = 0; i < inventory.getSize(); i++) {
      var item = inventory.getItem(i);

      if (item == null || !player.hasCooldown(item.getType())) continue;
      var name = item.getType().name();
      var cooldown = player.getCooldown(item.getType());

      Data.set(player, name, PersistentDataType.INTEGER, cooldown);
    }

  }


  @EventHandler
  private void apply(PlayerJoinEvent event) {
    var player = event.getPlayer();
    var inventory = player.getInventory();

    for (int i = 0; i < inventory.getSize(); i++) {
      var item = inventory.getItem(i);

      if (item == null) continue;

      var type = item.getType();
      var name = type.name();
      var cooldown = Data.get(player, name, PersistentDataType.INTEGER);
      if (cooldown == null) continue;

      player.setCooldown(type, cooldown);
      Data.set(player, name, PersistentDataType.INTEGER, 0);
    }

  }


  @EventHandler (priority =  EventPriority.LOWEST)
  private void interact(PlayerInteractEvent event) {
    if (event.getItem() == null) return;
    var player = event.getPlayer();
    var item = event.getItem();
    var type = item.getType();
    var itemID = new ItemBuilder(type).getID();

    var hasMsgCooldown = Data.get(player, "messageCooldown", PersistentDataType.BOOLEAN);
    var cooldown = player.getCooldown(type)/20;


    // Returns the function if the item is one of the following below.
    if (!player.hasCooldown(type) || itemID.equals("twisted_apple") || type.equals(Material.CARROT_ON_A_STICK) || type.equals(Material.ENDER_PEARL)) return;
    // Returns if the message cooldown is true
    if (hasMsgCooldown == null || hasMsgCooldown) return;

    Data.set(player, "messageCooldown", PersistentDataType.BOOLEAN, true);

    player.sendMessage(ChatUtils.error("#C15757Este item tiene cooldown. &8(#B36E6E" + cooldown + "s&8)"));
    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 0.5f);

    Bukkit.getScheduler().runTaskLater(Epiphany.getInstance(), () -> Data.set(player, "messageCooldown", PersistentDataType.BOOLEAN, false), 20);

  }


}
