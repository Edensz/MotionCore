package me.cinematic.server;

import me.cinematic.Cinematic;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileApi {


  public static void createYAMLFile(File folder, String fileName) {
    var file = new File(folder, fileName.toLowerCase() + ".yml");

    if (file.exists()) return;

    try {file.createNewFile();}
    catch (IOException ignored) {}
  }

  public static void createFolder(String fileName) {
    var file = new File(Cinematic.getInstance().getDataFolder(), fileName);

    if (!file.exists()) file.mkdirs();
  }
  public static void createFolder(String fileName, File folder) {
    var file = new File(folder, fileName);

    if (!file.exists()) file.mkdirs();
  }


  public static Configuration getConfigYML() {return Cinematic.getInstance().getConfig();}

  public static File getDataFolder() {return Cinematic.getInstance().getDataFolder();}

  public static File getFolder(String folderName) {
    return new File(Cinematic.getInstance().getDataFolder(), folderName);
  }
  public static File getFolder(String folderName, File root) {
    return new File(root, folderName);
  }

  public static File getFile(File dataFolder, String fileName) {
    return new File(dataFolder, fileName.toLowerCase() + ".yml");
  }

  public static YamlConfiguration getFileConfig(File file) {
    return YamlConfiguration.loadConfiguration(file);
  }


}
