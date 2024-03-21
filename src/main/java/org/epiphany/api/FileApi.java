package org.epiphany.api;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.epiphany.Epiphany;

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
    var file = new File(Epiphany.getInstance().getDataFolder(), fileName);

    if (!file.exists()) file.mkdirs();
  }


  public static Configuration getConfigYML() {return Epiphany.getInstance().getConfig();}

  public static File getDataFolder() {return Epiphany.getInstance().getDataFolder();}

  public static File getFolder(String folderName) {
    return new File(Epiphany.getInstance().getDataFolder(), folderName);
  }

  public static File getFile(File dataFolder, String fileName) {
    return new File(dataFolder, fileName.toLowerCase() + ".yml");
  }

  public static YamlConfiguration getFileConfig(File file) {
    return YamlConfiguration.loadConfiguration(file);
  }


}
