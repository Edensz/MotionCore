package org.motion.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.motion.tool.CinematicHelper;
import org.motion.tool.CinematicManager;
import org.motion.utils.PluginFileAPI;


public class PlayerListener implements Listener {

  @EventHandler (priority = EventPriority.HIGHEST)
  private void quit(PlayerQuitEvent event) {
    final var player = event.getPlayer();
    final var playerFileHelper = new PlayerFileHelper(player);

    if (playerFileHelper.getStatusMode(PlayerFileHelper.Status.RECORDING)) {
      new CinematicManager(player).finish();
    }
  }


  @EventHandler
  private void join(PlayerJoinEvent event) {
    final var player = event.getPlayer();
    final var playerFileHelper = new PlayerFileHelper(player);
    final var isFileCreated = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName()).exists();

    if (isFileCreated && !playerFileHelper.getStatusMode(PlayerFileHelper.Status.CHILLING)) {
      CinematicHelper.stopToolOnPlayer(player);
    }

    else new PlayerFileManager(player).create();
  }

}
