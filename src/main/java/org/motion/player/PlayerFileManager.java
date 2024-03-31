package org.motion.player;

import org.bukkit.entity.Player;
import org.motion.MotionCore;
import org.motion.utils.PluginFileAPI;

import java.io.File;
import java.io.IOException;


public class PlayerFileManager {
  public PlayerFileManager(Player player) {
    this.player = player;
  }

  public Player player;
  private static final File dataFolder = MotionCore.getInstance().getDataFolder();
  public static final File playerFolder = PluginFileAPI.getFolder("players", dataFolder);


  public void create() {
    PluginFileAPI.createYAMLFile(playerFolder, player.getName());
    update(false, false, true, true, true);
  }


  public void update(boolean watching, boolean recording, boolean chilling, boolean updateLocation, boolean updateGamemode) {
    final var file = PluginFileAPI.getFile(playerFolder, player.getName());
    final var config = PluginFileAPI.getFileConfig(file);
    final var location = player.getLocation();

    config.set("watching", watching);
    config.set("recording", recording);
    config.set("chilling", chilling);

    config.set("movie.creating", false);

    if (updateGamemode) config.set("gamemode", player.getGameMode().name().toLowerCase());

    if (updateLocation) {
      config.set("location.x_value", location.getX());
      config.set("location.y_value", location.getY());
      config.set("location.z_value", location.getZ());
      config.set("location.pitch", location.getPitch());
      config.set("location.yaw", location.getYaw());
      config.set("location.world", location.getWorld().getName());
    }

    try {config.save(file);}
    catch (IOException ignored) {}
  }

}
