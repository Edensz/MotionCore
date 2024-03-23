package org.motion.tool;

import org.motion.MotionCore;
import org.motion.player.PlayerFileHelper;
import org.motion.player.PlayerFileManager;
import org.motion.utils.ChatUtils;
import org.motion.utils.PluginFileAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class CinematicHelper {
  protected static ScheduledFuture<?> cinematicTask;
  protected static int durationTask;

  protected static String cinematicName;
  protected static int frameRate;
  protected static int frame = 1;
  protected static int duration = 0;

  protected static GameMode previousGamemode;
  protected static Location previousLocation;


  public static void stopToolOnPlayer(Player player) {
    final var file = PluginFileAPI.getFile(PlayerFileManager.playerFolder, player.getName());
    final var config = PluginFileAPI.getFileConfig(file);

    final var gamemode = config.getString("gamemode");
    final var world = config.getString("location.world");
    final var x = config.getDouble("location.x_value");
    final var y = config.getDouble("location.y_value");
    final var z = config.getDouble("location.z_value");
    final var pitch = config.getDouble("location.pitch");
    final var yaw = config.getDouble("location.yaw");

    if (world == null || gamemode == null) return;

    player.teleport(new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch));

    switch (gamemode) {
      case "survival" -> player.setGameMode(GameMode.SURVIVAL);
      case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
      case "creative" -> player.setGameMode(GameMode.CREATIVE);
      case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
    }

    new PlayerFileHelper(player).updateStatusAndLocation(PlayerFileHelper.Status.CHILLING);
  }


  protected static YamlConfiguration getProperties(String name) {
    var folder = PluginFileAPI.getFolder("cinematics");
    var cinematic = PluginFileAPI.getFolder(name, folder);
    var properties = PluginFileAPI.getFile(cinematic, "properties");

    return PluginFileAPI.getFileConfig(properties);
  }


  protected static void record(Player player, File cinematicFolder, int frameRate) {

    durationTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(MotionCore.getInstance(), () -> {
      player.sendActionBar(ChatUtils.format("&c• &7Duración de la cinemática&8: &f" + duration + "s &c•"));
      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,1,1.25f);
      duration = duration + 1;
    }, 0, 20);

    cinematicTask = MotionCore.getInstance().getService().scheduleWithFixedDelay(() -> {
      PluginFileAPI.createYAMLFile(cinematicFolder, "Frame" + frame);

      var playerLocation = player.getLocation();
      var frameFile = PluginFileAPI.getFile(cinematicFolder, "Frame" + frame);
      var frameConfig = PluginFileAPI.getFileConfig(frameFile);

      frameConfig.set("x", playerLocation.getX());
      frameConfig.set("y", playerLocation.getY());
      frameConfig.set("z", playerLocation.getZ());
      frameConfig.set("pitch", playerLocation.getPitch());
      frameConfig.set("yaw", playerLocation.getYaw());
      frameConfig.set("world", playerLocation.getWorld().getName());

      frame = frame + 1;

      try {frameConfig.save(frameFile);}
      catch (IOException ignored) {}
    }, 0, 1000/frameRate, TimeUnit.MILLISECONDS);

  }


}
