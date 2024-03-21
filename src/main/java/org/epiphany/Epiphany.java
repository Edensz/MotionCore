package org.epiphany;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.epiphany.api.FileApi;
import org.epiphany.api.Registrable;
import org.epiphany.epiphany.mechanics.time.TimeBar;
import org.epiphany.server.FileManager;
import org.epiphany.server.commands.GlobalCommand;
import org.epiphany.server.commands.StaffCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.epiphany.epiphany.player.MemberList;
import org.epiphany.epiphany.player.PlayerTask;
import org.epiphany.server.ServerTablist;
import org.reflections.Reflections;


public final class Epiphany extends JavaPlugin {
  private static @Getter Epiphany instance;
  private @Getter PaperCommandManager paperCommandManager;



  @Override
  public void onEnable() {
    instance = this;
    paperCommandManager = new PaperCommandManager(this);


    start();
    registerCommands(paperCommandManager, new GlobalCommand(), new StaffCommand());
    saveDefaultConfig();

    ServerTablist.animate();
    ServerTablist.display();
    PlayerTask.run();
    TimeBar.create();
    consoleLog("Time Bar Create");
    FileApi.createFolder("PlayerData");
    consoleLog("Create Player Data");


    for (MemberList whitelist : MemberList.toList()) {FileManager.createPlayerFile(whitelist.getName);}
    for (Player each : Bukkit.getOnlinePlayers()) {TimeBar.add(each);}


    Bukkit.getConsoleSender().sendMessage("[Epiphany] » El proyecto se ha encendido correctamente.");
  }



  @Override
  public void onDisable() {

    TimeBar.removeAll();

    Bukkit.getConsoleSender().sendMessage("[Epiphany] » El proyecto se ha apagado correctamente.");
  }



  private void start() {
    var reflections = new Reflections("org.epiphany");

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
    Bukkit.getConsoleSender().sendMessage("[Epiphany] » " + text);
  }


}
