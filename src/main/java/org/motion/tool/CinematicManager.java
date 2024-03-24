package org.motion.tool;

import org.bukkit.configuration.file.YamlConfiguration;
import org.motion.MotionCore;
import org.motion.player.PlayerFileHelper;
import org.motion.utils.ChatUtils;
import org.motion.utils.PlayerHandler;
import org.motion.utils.PluginFileAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class CinematicManager {
  private final Player player;
  private final String name;
  private File cinematicFolder = null;
  private File cinematicProperties = null;
  private YamlConfiguration cinematicConfig = null;

  public CinematicManager(Player player, @Nullable String cinematicName) {
    this.player = player;
    this.name = cinematicName;

    if (cinematicName != null) {
      this.cinematicFolder = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
      this.cinematicProperties = PluginFileAPI.getFile(cinematicFolder, "properties");
      this.cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);
    }
  }


  public void play() {
    final ScheduledFuture<?>[] task = new ScheduledFuture<?>[1];
    final var frameRate = cinematicConfig.getInt("properties.frameRate");
    final var framesRecorded = cinematicConfig.getInt("properties.framesRecorded");
    final int[] frame = {1};
    final int[] duration = {0};
    final int[] framesPerSecond = {0};

    PlayerFileHelper.updateStatusAndLocation(player, PlayerFileHelper.Status.WATCHING);
    player.setGameMode(GameMode.SPECTATOR);

    task[0] = MotionCore.getInstance().getService().scheduleWithFixedDelay(() -> {
      if (frame[0] >= framesRecorded || !player.isOnline()) {
        if (player.isOnline()) {
          Bukkit.getScheduler().runTask(MotionCore.getInstance(), this::finish);
        }

        task[0].cancel(true);
        task[0] = null;
      }

      if (frame[0] == 1 || framesPerSecond[0] == frameRate) {
        framesPerSecond[0] = 0;
        duration[0]++;
      }

      final var file = PluginFileAPI.getFile(cinematicFolder, "second" + duration[0]);
      final var config = PluginFileAPI.getFileConfig(file);
      final var path = "frame" + frame[0] + ".";
      final var world = config.getString(path + "world");

      if (world == null) return;

      Bukkit.getScheduler().runTask(MotionCore.getInstance(), () ->
        player.teleport(new Location(
          Bukkit.getWorld(world),
          config.getDouble(path + "x_value"),
          config.getDouble(path + "y_value"),
          config.getDouble(path + "z_value"),
          (float) CinematicHelper.roundTo(config.getDouble(path + "yaw"), 1),
          (float) CinematicHelper.roundTo(config.getDouble(path + "pitch"), 1)
        )));

      framesPerSecond[0]++;
      frame[0]++;
    }, 0, (1000/frameRate) * 1000L, TimeUnit.MICROSECONDS);
  }


  public void finish() {
    var dataFolder = MotionCore.getInstance().getDataFolder();
    var playersFolder = PluginFileAPI.getFolder("players", dataFolder);
    var playerFile = PluginFileAPI.getFile(playersFolder, player.getName());
    var playerConfig = PluginFileAPI.getFileConfig(playerFile);

    final var gamemode = playerConfig.getString("gamemode");
    final var world = playerConfig.getString("location.world");
    final var x = playerConfig.getDouble("location.x_value");
    final var y = playerConfig.getDouble("location.y_value");
    final var z = playerConfig.getDouble("location.z_value");
    final var pitch = playerConfig.getDouble("location.pitch");
    final var yaw = playerConfig.getDouble("location.yaw");

    if (world == null || gamemode == null) return;

    player.teleport(new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch));

    switch (gamemode) {
      case "survival" -> player.setGameMode(GameMode.SURVIVAL);
      case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
      case "creative" -> player.setGameMode(GameMode.CREATIVE);
      case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
    }

    PlayerHandler.playSound(Sound.BLOCK_CHEST_CLOSE,0.85F, player);
    PlayerFileHelper.updateStatusAndLocation(player, PlayerFileHelper.Status.CHILLING);
  }


  public void create(int frameRate) {
    PluginFileAPI.createFolderInFolder(name, CinematicHelper.cinematicsFolder);
    final var cinematic = PluginFileAPI.getFolder(name, CinematicHelper.cinematicsFolder);

    PluginFileAPI.createYAMLFile(cinematic, "properties");
    PlayerFileHelper.updateStatusAndLocation(player, PlayerFileHelper.Status.RECORDING);

    cinematicConfig.set("properties.framesRecorded", 0);
    cinematicConfig.set("properties.totalDuration", 0);
    cinematicConfig.set("properties.author", 0);
    cinematicConfig.set("properties.frameRate", 0);

    player.setGameMode(GameMode.SPECTATOR);
    PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.25f, player);
    PlayerHandler.playSound(Sound.UI_BUTTON_CLICK, 1.25f, player);

    this.record(frameRate);
  }


  private void record(int frameRate) {
    final ScheduledFuture<?>[] task = new ScheduledFuture<?>[1];
    final int[] frame = {1};
    final int[] duration = {0};
    final int[] maxFPS = {0};

    task[0] = MotionCore.getInstance().getService().scheduleWithFixedDelay(() -> {
      if (frame[0] == 1 || maxFPS[0] == frameRate) {
        player.sendActionBar(ChatUtils.format("&c• &7Tiempo grabado&8: &f" + duration[0] + "s &c•"));
        PlayerHandler.playSound(Sound.UI_BUTTON_CLICK, 1.55f, player);

        maxFPS[0] = 0;
        duration[0]++;

        PluginFileAPI.createYAMLFile(cinematicFolder, "second" + duration[0]);
      }

      final var playerLocation = player.getLocation();
      final var secondFile = PluginFileAPI.getFile(cinematicFolder, "second" + duration[0]);
      final var secondConfig = PluginFileAPI.getFileConfig(secondFile);
      final var path = "frame" + frame[0] + ".";

      secondConfig.set(path + "x_value", playerLocation.getX());
      secondConfig.set(path + "y_value", playerLocation.getY());
      secondConfig.set(path + "z_value", playerLocation.getZ());
      secondConfig.set(path + "pitch", playerLocation.getPitch());
      secondConfig.set(path + "yaw", playerLocation.getYaw());
      secondConfig.set(path + "world", playerLocation.getWorld().getName());

      maxFPS[0]++;
      frame[0]++;

      try {secondConfig.save(secondFile);}
      catch (IOException ignored) {}

      if (!PlayerFileHelper.getStatusMode(player, PlayerFileHelper.Status.RECORDING) || !player.isOnline()) {
        cinematicConfig.set("properties.framesRecorded", frame[0]);
        cinematicConfig.set("properties.totalDuration", duration[0]);
        cinematicConfig.set("properties.author", player.getName());
        cinematicConfig.set("properties.frameRate", frameRate);

        try {
          cinematicConfig.save(cinematicProperties);
        } catch (IOException ignored) {}

        task[0].cancel(true);
      }
    }, 0, (1000/frameRate) * 1000L, TimeUnit.MICROSECONDS);
  }

}
