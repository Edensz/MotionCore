package org.motion.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.motion.tool.CinematicHelper;
import org.motion.tool.CinematicManager;
import org.motion.utils.PluginFileAPI;


public class PlayerListener implements Listener {

  @EventHandler
  private void join(PlayerJoinEvent event) {
    final var player = event.getPlayer();
    final var isFileCreated = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName()).exists();

    if (isFileCreated && !PlayerFileHelper.getStatusMode(player, PlayerFileHelper.Status.CHILLING)) {
      new CinematicManager(player, null).finish();
    }

    else new PlayerFileManager(player).create();
  }

}
