package org.motion.tool;

import org.motion.MotionCore;
import org.motion.utils.PlayerHandler;
import org.motion.utils.ChatUtils;
import org.motion.utils.PluginFileAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class CinematicManager {
  private final File cinematicsFolder = PluginFileAPI.getFolder("cinematics");
  private static ScheduledFuture<?> task;


  protected void play(String name, Player audience) {
    final var previousLocation = audience.getLocation();
    final var previousGamemode = audience.getGameMode();
    final int[] frame = {1};

    final var properties = CinematicHelper.getProperties(name);

    audience.setGameMode(GameMode.SPECTATOR);

    task = MotionCore.getInstance().getService().scheduleWithFixedDelay(() -> {
      if (frame[0] >= properties.getInt("totalFrames")) {
        Bukkit.getScheduler().runTask(MotionCore.getInstance(), () -> {
          audience.setGameMode(previousGamemode);
          audience.teleport(previousLocation);
        });

        frame[0] = 1;
        task.cancel(true);
        task = null;
      }

      var frameFile = PluginFileAPI.getFile(PluginFileAPI.getFolder(name, cinematicsFolder), "Frame" + frame[0]);
      var frameConfig = PluginFileAPI.getFileConfig(frameFile);

      final var world = frameConfig.getString("world");
      final var x = frameConfig.getDouble("x");
      final var y = frameConfig.getDouble("y");
      final var z = frameConfig.getDouble("z");
      final var pitch = frameConfig.getDouble("pitch");
      final var yaw = frameConfig.getDouble("yaw");

      if (world == null) return;

      Bukkit.getScheduler().runTask(MotionCore.getInstance(), () -> audience.teleport(new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch)));

      frame[0]++;
    }, 0, 1000/properties.getInt("frameRate"), TimeUnit.MILLISECONDS);
  }


  public void finish(Player player) {
    var cinematic = PluginFileAPI.getFolder(CinematicHelper.cinematicName, cinematicsFolder);
    var file = PluginFileAPI.getFile(cinematic, "properties");
    var properties = PluginFileAPI.getFileConfig(file);

    properties.set("totalFrames", CinematicHelper.frame);
    properties.set("duration", CinematicHelper.duration);

    player.teleport(CinematicHelper.previousLocation);
    player.setGameMode(CinematicHelper.previousGamemode);
    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100,5));

    MotionCore.getInstance().getConfig().set("recording", false);
    CinematicHelper.frameRate = 0;
    CinematicHelper.frame = 1;
    CinematicHelper.duration = 0;
    CinematicHelper.cinematicTask.cancel(true);
    Bukkit.getScheduler().cancelTask(CinematicHelper.durationTask);

    try {properties.save(file);}
    catch (IOException ignored) {}
  }


  public void create(String name, Player player, int frameRate) {
    PluginFileAPI.createFolderInFolder(name, cinematicsFolder);

    var cinematic = PluginFileAPI.getFolder(name, cinematicsFolder);

    PluginFileAPI.createYAMLFile(cinematic, "properties");

    var file = PluginFileAPI.getFile(cinematic, "properties");
    var properties = PluginFileAPI.getFileConfig(file);

    properties.set("author", player.getName());
    properties.set("frameRate", frameRate);

    CinematicHelper.frameRate = frameRate;
    CinematicHelper.cinematicName = name;
    CinematicHelper.previousLocation = player.getLocation();
    CinematicHelper.previousGamemode = player.getGameMode();

    MotionCore.getInstance().getConfig().set("recording", true);
    player.setGameMode(GameMode.SPECTATOR);
    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN,1,1.25f);

    CinematicHelper.record(player, cinematic, frameRate);

    try {properties.save(file);}
    catch (IOException ignored) {}
  }


}
