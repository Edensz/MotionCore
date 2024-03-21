package org.epiphany.epiphany.player;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;
import org.epiphany.api.Data;
import org.epiphany.api.Registrable;
import org.epiphany.epiphany.mechanics.time.TimeBar;

@Registrable
public class PlayerListener implements Listener {


  @EventHandler
  private void move(PlayerMoveEvent event) {
    var player = event.getPlayer();

    var freeze = Data.get(player, "freeze", PersistentDataType.BOOLEAN);
    if (freeze == null) return;

    if (freeze) event.setCancelled(true);
  }



  @EventHandler
  private void changeWorld(PlayerChangedWorldEvent event) {
    var world = Bukkit.getWorld("world");
    var from = event.getFrom();
    var player = event.getPlayer();

    if (from.equals(world)) TimeBar.remove(player);
    else TimeBar.add(player);
  }



  @EventHandler
  private void death(PlayerDeathEvent event) {
    var player = event.getPlayer();

    var deaths = Data.get(player, "deaths", PersistentDataType.INTEGER);
    if (deaths == null) return;

    Data.set(player, "deaths", PersistentDataType.INTEGER, deaths + 1);
  }


}
