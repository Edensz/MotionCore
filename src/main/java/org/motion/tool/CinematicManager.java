package org.motion.tool;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.motion.MotionCore;
import org.motion.player.PlayerFileHelper;
import org.motion.utils.CancellableScheduledTask;
import org.motion.utils.ChatUtils;
import org.motion.player.PlayerHandler;
import org.motion.utils.PluginFileAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CinematicManager {
  private final Player player;
  private final String cinematicName;
  private File cinematicFolder = null;
  private File cinematicProperties = null;
  private YamlConfiguration cinematicConfig = null;

  public CinematicManager(Player player, @Nullable String cinematicName) {
    this.player = player;
    this.cinematicName = cinematicName;
    if (cinematicName != null) {
      this.cinematicFolder = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
      this.cinematicProperties = PluginFileAPI.getFile(cinematicFolder, "properties");
      this.cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);
    }
  }


  public final void play(@Nullable Player sender) {
    if (!CinematicHelper.doesCinematicExist(cinematicName)) {
      PlayerHandler.errorMessage("¡La cinemática insertada no existe!", player);
      return;
    }

    final var cinematicType = cinematicConfig.getString("properties.type");
    if (cinematicType == null) return;
    final var movieCinematic = cinematicType.equals("movie");

    final var frameRate = cinematicConfig.getInt("properties.frameRate");
    final var framesRecorded = cinematicConfig.getInt("properties.framesRecorded");
    final int[] frame = {1};
    final int[] duration = {0};
    final int[] framesPerSecond = {0};

    if (sender != null) {
      if (framesRecorded == 0) {
        PlayerHandler.errorMessage("La cinemática que intentaste reproducir no cuenta con ningún fotograma grabado, por lo tanto, será eliminada automáticamente.", sender);
        PluginFileAPI.deleteFolderInFolder("cinematics", cinematicName);
        return;
      }
      PlayerHandler.sendMessage("#CFB39CReproduciendo la cinemática #D78E51" + cinematicName + "#CFB39C a #D78E51" + player.getName() + "#CFB39C.", sender);
      PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP,1.35f, sender);
      PlayerHandler.playSound(Sound.BLOCK_PISTON_EXTEND,1.35f, sender);
    }

    var initialDelay = 0;
    if (movieCinematic) {
      final var interpolation = cinematicConfig.getBoolean("movie.interpolation");
      if (interpolation) {
        CinematicHelper.displayTransitionScreen(player);
        initialDelay = 40;
      }
    }

    PlayerFileHelper.updatePlayerFile(player, PlayerFileHelper.Status.WATCHING, false);
    Bukkit.getScheduler().runTaskLater(MotionCore.getInstance(), () -> {
      player.setGameMode(GameMode.SPECTATOR);
      for (Player each : Bukkit.getOnlinePlayers()) {
        if (!each.getGameMode().equals(GameMode.SPECTATOR)) continue;
        player.hidePlayer(MotionCore.getInstance(), each);
      }

      final var cinematicFolder = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
      final var cinematicProperties = PluginFileAPI.getFile(cinematicFolder, "properties");
      final var cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);
      var timesPlayed = cinematicConfig.getInt("properties.timesPlayed");
      var playersWatching = cinematicConfig.getInt("properties.playersWatching");
      cinematicConfig.set("properties.timesPlayed", timesPlayed + 1);
      cinematicConfig.set("properties.playersWatching", playersWatching + 1);
      try {cinematicConfig.save(cinematicProperties);}
      catch (IOException ignored) {}

      var task = new CancellableScheduledTask() {
        @Override
        public void run() {
          if (frame[0] >= framesRecorded || !player.isOnline() || PlayerFileHelper.isStatusDeactivated(player, PlayerFileHelper.Status.WATCHING) || !CinematicHelper.doesCinematicExist(cinematicName)) {
            // Called again to get the NEW player watching number :p
            final var cinematicFolder = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
            final var cinematicProperties = PluginFileAPI.getFile(cinematicFolder, "properties");
            final var cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);
            var playersWatching = cinematicConfig.getInt("properties.playersWatching");
            cinematicConfig.set("properties.playersWatching", playersWatching - 1);
            try {cinematicConfig.save(cinematicProperties);}
            catch (IOException ignored) {}

            this.cancel();
            if (player.isOnline()) {
              Bukkit.getScheduler().runTask(MotionCore.getInstance(), () -> finish());
              return;
            }
          }

          if (movieCinematic) {
            final var interpolation = cinematicConfig.getBoolean("movie.interpolation");
            if (interpolation && frame[0] == framesRecorded - (frameRate + (frameRate/2))) CinematicHelper.displayTransitionScreen(player);
            if (frame[0] == 1) {
              final var musicToPlay = cinematicConfig.getString("movie.musicToPlay");
              if (musicToPlay != null) player.playSound(player.getLocation(), musicToPlay, SoundCategory.AMBIENT, 1, 1);
            }
          }

          if (frame[0] == 1 || framesPerSecond[0] == frameRate) {
            framesPerSecond[0] = 0;
            duration[0]++;
          }

          final var secondFile = PluginFileAPI.getFile(cinematicFolder, "second" + duration[0]);
          final var secondConfig = PluginFileAPI.getFileConfig(secondFile);
          final var framePath = "frame" + frame[0] + ".";
          final var world = secondConfig.getString(framePath + "world");
          if (world == null) return;

          final var letterBoxBars = cinematicConfig.getBoolean("movie.letterBoxBars");
          if (letterBoxBars && (frame[0] < (framesRecorded - 100))) player.sendActionBar("㊡");

          Bukkit.getScheduler().runTask(MotionCore.getInstance(), () -> player.teleport(new Location(
                  Bukkit.getWorld(world),
                  secondConfig.getDouble(framePath + "x_value"),
                  secondConfig.getDouble(framePath + "y_value"),
                  secondConfig.getDouble(framePath + "z_value"),
                  (float) CinematicHelper.roundToNearestTenth(secondConfig.getDouble(framePath + "yaw")),
                  (float) CinematicHelper.roundToNearestTenth(secondConfig.getDouble(framePath + "pitch"))
          )));
          framesPerSecond[0]++;
          frame[0]++;
        }
      };

      task.setScheduledFuture(MotionCore.getExecutorService().scheduleWithFixedDelay(task, 0, (1000/frameRate) * 1000L, TimeUnit.MICROSECONDS));
    }, initialDelay);
  }


  public final void finish() {
    var dataFolder = MotionCore.getInstance().getDataFolder();
    var playersFolder = PluginFileAPI.getFolder("players", dataFolder);
    var playerFile = PluginFileAPI.getFile(playersFolder, player.getName());
    var playerConfig = PluginFileAPI.getFileConfig(playerFile);

    final var gamemode = playerConfig.getString("gamemode");
    final var world = playerConfig.getString("location.world");
    if (world == null || gamemode == null) return;

    final var x = playerConfig.getDouble("location.x_value");
    final var y = playerConfig.getDouble("location.y_value");
    final var z = playerConfig.getDouble("location.z_value");
    final var pitch = playerConfig.getDouble("location.pitch");
    final var yaw = playerConfig.getDouble("location.yaw");
    player.teleport(new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch));
    player.stopAllSounds();

    switch (gamemode) {
      case "survival" -> player.setGameMode(GameMode.SURVIVAL);
      case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
      case "creative" -> player.setGameMode(GameMode.CREATIVE);
      case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
    }

    for (Player each : Bukkit.getOnlinePlayers()) player.showPlayer(MotionCore.getInstance(), each);
    PlayerFileHelper.updatePlayerFile(player, PlayerFileHelper.Status.CHILLING, false);
  }


  public final void create(int frameRate, @NotNull CinematicHelper.CinematicType cinematicType, boolean startRecording) {
    PluginFileAPI.createFolderInFolder(cinematicName, CinematicHelper.cinematicsFolder);
    final var cinematic = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
    PluginFileAPI.createYAMLFile(cinematic, "properties");

    cinematicConfig.set("properties.totalDuration", 0);
    cinematicConfig.set("properties.author", 0);
    cinematicConfig.set("properties.frameRate", 0);
    cinematicConfig.set("properties.playersWatching", 0);
    cinematicConfig.set("properties.timesPlayed", 0);
    cinematicConfig.set("properties.type", cinematicType.name().toLowerCase());

    if (cinematicType != CinematicHelper.CinematicType.BASIC) {
      cinematicConfig.set("properties.author", player.getName());
      cinematicConfig.set("properties.frameRate", frameRate);
    }
    if (cinematicType != CinematicHelper.CinematicType.CHAIN) {
      cinematicConfig.set("properties.chainUsages", 0);
      cinematicConfig.set("properties.framesRecorded", 0);
    }

    switch (cinematicType) {
      case MOVIE -> {
        cinematicConfig.set("movie.interpolation", true);
        cinematicConfig.set("movie.letterBoxBars", true);
        cinematicConfig.set("movie.playMusic", true);
        cinematicConfig.set("movie.musicToPlay", "music_disc.stal");
      }
      case CHAIN -> {
        cinematicConfig.set("chain.cinematics", 0);
        cinematicConfig.set("chain.stopAudioOnInterval", true);
        cinematicConfig.set("chain.playOnlyFirstMusic", true);
      }
    }

    try {cinematicConfig.save(cinematicProperties);}
    catch (IOException ignored) {}

    if (!startRecording) return;
    PlayerFileHelper.updatePlayerFile(player, PlayerFileHelper.Status.RECORDING, false);
    player.setGameMode(GameMode.SPECTATOR);
    this.record(frameRate, false);
  }


  public final void record(int frameRate, boolean movieCinematic) {
    final int[] frame = {1};
    final int[] duration = {0};
    final int[] maxFPS = {0};

    if (movieCinematic) {
      final var cinematicFolder = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
      final var cinematicProperties = PluginFileAPI.getFile(cinematicFolder, "properties");
      final var cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);

      final var musicToPlay = cinematicConfig.getString("movie.musicToPlay");
      if (musicToPlay != null) player.playSound(player.getLocation(), musicToPlay, SoundCategory.AMBIENT, 1, 1);
    }

    player.setGameMode(GameMode.SPECTATOR);
    var task = new CancellableScheduledTask() {
      @Override
      public void run() {
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

        if (PlayerFileHelper.isStatusDeactivated(player, PlayerFileHelper.Status.RECORDING) || !player.isOnline()) {
          if (movieCinematic) player.stopAllSounds();
          cinematicConfig.set("properties.framesRecorded", frame[0]);
          cinematicConfig.set("properties.totalDuration", duration[0]);
          cinematicConfig.set("properties.author", player.getName());
          cinematicConfig.set("properties.frameRate", frameRate);
          try { cinematicConfig.save(cinematicProperties); }
          catch (IOException ignored) {}
          this.cancel();
        }
      }
    };

    task.setScheduledFuture(MotionCore.getExecutorService().scheduleWithFixedDelay(task, 0, (1000/frameRate) * 1000L, TimeUnit.MICROSECONDS));
  }

}