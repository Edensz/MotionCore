package org.motion.player;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.motion.MotionCore;
import org.motion.tool.CinematicHelper;
import org.motion.tool.CinematicManager;
import org.motion.tool.CinematicPanel;
import org.motion.utils.MenuAPI;
import org.motion.utils.PluginFileAPI;

import java.io.IOException;


public class PlayerListener implements Listener {

  @EventHandler
  private void chat(AsyncPlayerChatEvent event) {
    final var player = event.getPlayer();
    final var playerFile = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName());
    final var playerConfig = PluginFileAPI.getFileConfig(playerFile);
    final var message = event.getMessage();

    if (!(playerConfig.getBoolean("movie.creating"))) return;

    event.setCancelled(true);

    final var cinematicName = playerConfig.getString("movie.name");
    final var cinematicFolder = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
    final var cinematicProperties = PluginFileAPI.getFile(cinematicFolder, "properties");
    final var cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);

    PlayerHandler.playSound(Sound.BLOCK_CHEST_OPEN, 0.95f, player);
    PlayerHandler.playSound(Sound.BLOCK_ENDER_CHEST_OPEN, 0.85f, player);

    if (message.contains(" ")) {
      Bukkit.getScheduler().runTask(MotionCore.getInstance(), () -> new CinematicPanel(player, MenuAPI.PanelMode.MOVIE, cinematicName).open());
      PlayerHandler.errorMessage("El sonido insertado es inválido.", player);
      return;
    }

    PlayerFileHelper.setCreatingMode(player, false, "N/A");
    cinematicConfig.set("movie.musicToPlay", message.toLowerCase());
    cinematicConfig.set("movie.playMusic", true);

    try {cinematicConfig.save(cinematicProperties);}
    catch (IOException ignored) {}

    Bukkit.getScheduler().runTask(MotionCore.getInstance(), () -> new CinematicPanel(player, MenuAPI.PanelMode.MOVIE, cinematicName).open());
  }


  @EventHandler
  private void join(PlayerJoinEvent event) {
    final var player = event.getPlayer();
    final var isFileCreated = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName()).exists();

    if (isFileCreated && PlayerFileHelper.isStatusDeactivated(player, PlayerFileHelper.Status.CHILLING)) new CinematicManager(player, null).finish();
    else new PlayerFileManager(player).create();
  }

}
