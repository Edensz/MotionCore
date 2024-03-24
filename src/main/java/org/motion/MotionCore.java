package org.motion;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.motion.player.PlayerFileHelper;
import org.motion.player.PlayerListener;
import org.motion.tool.CinematicManager;
import org.motion.utils.PluginFileAPI;
import org.motion.tool.CinematicCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public final class MotionCore extends JavaPlugin {
  private static @Getter MotionCore instance;
  public final @Getter ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();


  @Override
  public void onEnable() {
    MotionCore.logConsoleMessage("Cargando herramienta...");

    instance = this;
    saveDefaultConfig();

    new PaperCommandManager(this).registerCommand(new CinematicCommand());

    PluginFileAPI.createFolderInFolder("cinematics");
    PluginFileAPI.createFolderInFolder("players");
    Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

    MotionCore.logConsoleMessage("La herramienta se activó correctamente.");
  }


  @Override
  public void onDisable() {
    MotionCore.logConsoleMessage("Apagando herramienta...");

    for (Player each : Bukkit.getOnlinePlayers()) {
      if (!PlayerFileHelper.getStatusMode(each, PlayerFileHelper.Status.CHILLING)) {
        new CinematicManager(each, null).finish();
      }
    }

    MotionCore.logConsoleMessage("La herramienta se apagó correctamente.");
  }


  public static void logConsoleMessage(String text) {Bukkit.getConsoleSender().sendMessage("[MotionCore] » " + text);}

}
