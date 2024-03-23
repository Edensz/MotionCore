package org.motion.tool;

import org.motion.MotionCore;
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


  protected static YamlConfiguration getProperties(String name) {
    var folder = PluginFileAPI.getFolder("cinematics");
    var cinematic = PluginFileAPI.getFolder(name, folder);
    var properties = PluginFileAPI.getFile(cinematic, "properties");

    return PluginFileAPI.getFileConfig(properties);
  }


  protected static void record(Player player, File cinematicFolder, int frameRate) {

    durationTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(MotionCore.getInstance(), () -> {
      player.sendActionBar(ChatUtils.format("&c• &7Duración de la Cinemática&8: &f" + duration + "s &c•"));
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
