package me.cinematic.tool;

import me.cinematic.Cinematic;
import me.cinematic.player.PlayerHandler;
import me.cinematic.server.ChatUtils;
import me.cinematic.server.FileApi;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class CinematicManager {
  private final File cinematicsFolder = FileApi.getFolder("cinematics");
  private static ScheduledFuture<?> task;
  private int frame = 1;


  public void play(String name, Player audience) {
    final var previousLocation = audience.getLocation();
    final var previousGamemode = audience.getGameMode();

    var properties = CinematicHelper.getProperties(name);
    final var totalFrames = properties.getInt("totalFrames");
    final var frameRate = properties.getInt("frameRate");

    audience.setGameMode(GameMode.SPECTATOR);


    task = Cinematic.getInstance().getService().scheduleWithFixedDelay(() -> {
      if (frame >= totalFrames) {
        audience.setGameMode(previousGamemode);
        audience.teleport(previousLocation);

        frame = 1;
        task.cancel(true);
        task = null;
      }

      var frameFile = FileApi.getFile(FileApi.getFolder(name, cinematicsFolder), "Frame" + frame);
      var frameConfig = FileApi.getFileConfig(frameFile);

      final var world = frameConfig.getString("world");
      final var x = frameConfig.getDouble("x");
      final var y = frameConfig.getDouble("y");
      final var z = frameConfig.getDouble("z");
      final var pitch = frameConfig.getDouble("pitch");
      final var yaw = frameConfig.getDouble("yaw");

      if (world == null) return;

      Bukkit.getScheduler().runTask(Cinematic.getInstance(), () -> audience.teleport(new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch)));

      frame = frame + 1;
    }, 0, 1000/frameRate, TimeUnit.MILLISECONDS);
  }


  public void finish(Player player) {
    var cinematic = FileApi.getFolder(CinematicHelper.cinematicName, cinematicsFolder);
    var file = FileApi.getFile(cinematic, "properties");
    var properties = FileApi.getFileConfig(file);

    properties.set("totalFrames", CinematicHelper.frame);
    properties.set("duration", CinematicHelper.duration);

    player.teleport(CinematicHelper.previousLocation);
    player.setGameMode(CinematicHelper.previousGamemode);
    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,100,5));

    Cinematic.getInstance().getConfig().set("recording", false);
    PlayerHandler.sendMessage("¡La cinemática fue guardada correctamente!", player);
    player.sendMessage(ChatUtils.format("Duración: " + CinematicHelper.duration + "s, FPS: " + CinematicHelper.frameRate + ", Nombre: " + CinematicHelper.cinematicName));
    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE,1,1.25f);
    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1.25f);

    CinematicHelper.frameRate = 0;
    CinematicHelper.frame = 1;
    CinematicHelper.duration = 0;

    CinematicHelper.cinematicTask.cancel(true);
    Bukkit.getScheduler().cancelTask(CinematicHelper.durationTask);

    try {properties.save(file);}
    catch (IOException ignored) {}
  }


  public void create(String name, Player player, int frameRate) {
    FileApi.createFolder(name, cinematicsFolder);

    var cinematic = FileApi.getFolder(name, cinematicsFolder);

    FileApi.createYAMLFile(cinematic, "properties");

    var file = FileApi.getFile(cinematic, "properties");
    var properties = FileApi.getFileConfig(file);

    properties.set("author", player.getName());
    properties.set("frameRate", frameRate);

    CinematicHelper.frameRate = frameRate;
    CinematicHelper.cinematicName = name;
    CinematicHelper.previousLocation = player.getLocation();
    CinematicHelper.previousGamemode = player.getGameMode();

    Cinematic.getInstance().getConfig().set("recording", true);
    player.setGameMode(GameMode.SPECTATOR);
    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN,1,1.25f);

    CinematicHelper.record(player, cinematic, frameRate);

    try {properties.save(file);}
    catch (IOException ignored) {}
  }


}
