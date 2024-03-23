package org.motion;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.motion.utils.PluginFileAPI;
import org.motion.tool.GlobalCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public final class MotionCore extends JavaPlugin {
  private static @Getter MotionCore instance;
  public final @Getter ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();


  @Override
  public void onEnable() {
    Bukkit.getConsoleSender().sendMessage("[MotionCore] » Cargando herramienta...");

    instance = this;
    saveDefaultConfig();

    new PaperCommandManager(this).registerCommand(new GlobalCommand());
    PluginFileAPI.createFolderInFolder("cinematics");

    Bukkit.getConsoleSender().sendMessage("[MotionCore] » La herramienta se activó correctamente.");
  }


  @Override
  public void onDisable() {
    Bukkit.getConsoleSender().sendMessage("[MotionCore] » La herramienta se apagó correctamente.");
  }

}
