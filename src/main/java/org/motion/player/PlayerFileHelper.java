package org.motion.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.motion.utils.PluginFileAPI;


public class PlayerFileHelper {
  public enum Status {WATCHING,RECORDING,CHILLING}


  public static boolean getStatusMode(Player player, Status status) {
    var key = status.name().toLowerCase();
    var file = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName());
    var config = PluginFileAPI.getFileConfig(file);

    return config.getBoolean(key);
  }


  public static void updatePlayerFile(Player player, @NotNull Status status, boolean onlyStatus) {
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
