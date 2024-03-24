package org.motion.player;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.motion.utils.Data;
import org.motion.utils.PluginFileAPI;


public class PlayerFileHelper {
  public enum Status {WATCHING,RECORDING,CHILLING}


  public static boolean getStatusMode(Player player, @NotNull Status status) {
    var key = status.name().toLowerCase();
    var file = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName());
    var config = PluginFileAPI.getFileConfig(file);

    return config.getBoolean(key);
  }


  public static void updateStatusAndLocation(Player player, @NotNull Status status) {
    final var key = status.name().toLowerCase();

    final var file = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName());
    final var config = PluginFileAPI.getFileConfig(file);
    final var fileManager = new PlayerFileManager(player);

    for (Status playerStatus : Status.values()) {
      final var eachKey = playerStatus.name().toLowerCase();

      config.set(eachKey, "false");
      Data.set(player, eachKey, PersistentDataType.BOOLEAN, false);
    }

    switch (status) {
      case CHILLING -> fileManager.update(false, false, true);
      case RECORDING -> fileManager.update(false, true, false);
      case WATCHING -> fileManager.update(true, false, false);
    }

    Data.set(player, key, PersistentDataType.BOOLEAN, true);
    config.set(key, "true");
  }

}
