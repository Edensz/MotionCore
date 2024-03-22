package me.cinematic;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.cinematic.server.FileApi;
import me.cinematic.tool.GlobalCommand;
import me.cinematic.server.Registrable;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public final class Cinematic extends JavaPlugin {
  private static @Getter Cinematic instance;
  private @Getter PaperCommandManager paperCommandManager;
  public final @Getter ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();


  @Override
  public void onEnable() {
    instance = this;
    paperCommandManager = new PaperCommandManager(this);

    start();
    registerCommands(paperCommandManager, new GlobalCommand());
    saveDefaultConfig();

    FileApi.createFolder("cinematics");

    consoleLog("El proyecto se ha encendido correctamente.");
  }


  @Override
  public void onDisable() {
    consoleLog("El proyecto se ha apagado correctamente.");
  }



  private void start() {
    var reflections = new Reflections("me.cinematic");

    for (Class<?> clazz : reflections.getTypesAnnotatedWith(Registrable.class)) {
      try {
        if (clazz.getDeclaredConstructor().newInstance() instanceof Listener list) {
          Bukkit.getServer().getPluginManager().registerEvents(list, instance);
        }
      }
      catch (Exception ignored) {}
    }

  }

  private void registerCommands(PaperCommandManager manager, BaseCommand... commandExecutors) {
    for (BaseCommand commandExecutor : commandExecutors) {
      manager.registerCommand(commandExecutor);
    }
  }

  public void consoleLog(String text) {
    Bukkit.getConsoleSender().sendMessage("[CinematicTool] » " + text);
  }


}
