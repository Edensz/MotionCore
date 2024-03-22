package me.cinematic.tool;

import me.cinematic.Cinematic;
import me.cinematic.server.ChatUtils;
import me.cinematic.server.FileApi;
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
    var folder = FileApi.getFolder("cinematics");
    var cinematic = FileApi.getFolder(name, folder);
    var properties = FileApi.getFile(cinematic, "properties");

    return FileApi.getFileConfig(properties);
  }


  protected static void record(Player player, File cinematicFolder, int frameRate) {

    durationTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cinematic.getInstance(), () -> {
      player.sendActionBar(ChatUtils.format("&c• &7Duración de la Cinemática&8: &f" + duration + "s &c•"));
      player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,1,1.25f);
      duration = duration + 1;
    }, 0, 20);

    cinematicTask = Cinematic.getInstance().getService().scheduleWithFixedDelay(() -> {
      FileApi.createYAMLFile(cinematicFolder, "Frame" + frame);

      var playerLocation = player.getLocation();
      var frameFile = FileApi.getFile(cinematicFolder, "Frame" + frame);
      var frameConfig = FileApi.getFileConfig(frameFile);

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
