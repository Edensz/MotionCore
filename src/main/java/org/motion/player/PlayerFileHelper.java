package org.motion.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.motion.utils.PluginFileAPI;

import java.io.IOException;


public class PlayerFileHelper {
  public enum Status {WATCHING,RECORDING,CHILLING}


  public static void setCreatingMode(@NotNull Player player, boolean creatingMode, String cinematicName) {
    final var playerFile = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName());
    final var playerConfig = PluginFileAPI.getFileConfig(playerFile);

    playerConfig.set("movie.creating", creatingMode);
    playerConfig.set("movie.name", cinematicName);

    try {playerConfig.save(playerFile);}
    catch (IOException ignored) {}
  }


  public static boolean isStatusDeactivated(@NotNull Player player, @NotNull Status status) {
    var key = status.name().toLowerCase();
    var file = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName());
    return !PluginFileAPI.getFileConfig(file).getBoolean(key);
  }

  public static boolean isStatusActivated(@NotNull Player player, @NotNull Status status) {
    var key = status.name().toLowerCase();
    var file = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName());
    return PluginFileAPI.getFileConfig(file).getBoolean(key);
  }


  public static void updatePlayerFile(@NotNull Player player, @NotNull Status status, boolean onlyStatus) {
    final var key = status.name().toLowerCase();

    final var file = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName());
    final var config = PluginFileAPI.getFileConfig(file);
    final var fileManager = new PlayerFileManager(player);

    for (Status playerStatus : Status.values()) {
      final var eachKey = playerStatus.name().toLowerCase();

      config.set(eachKey, "false");
    }

    switch (status) {
      case CHILLING -> fileManager.update(false, false, true, !onlyStatus, !onlyStatus);
      case RECORDING -> fileManager.update(false, true, false, !onlyStatus, !onlyStatus);
      case WATCHING -> fileManager.update(true, false, false, !onlyStatus, !onlyStatus);
    }

    config.set(key, "true");
  }

}
