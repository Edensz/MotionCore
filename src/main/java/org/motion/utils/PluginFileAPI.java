package org.motion.utils;

import org.bukkit.Bukkit;
import org.motion.MotionCore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginFileAPI {

  //* - Useful and fundamental functions for file creation. -

  //? Method to create a YAML file in a specified folder
  public static void createYAMLFile(File folder, String fileName) {
    // Create a new File object representing the YAML file
    var file = new File(folder, fileName.toLowerCase() + ".yml");

    // If the file already exists, return without doing anything
    if (file.exists()) return;

    try {
      // Attempt to create a new file
      file.createNewFile();
    } catch (IOException ignored) {
      // Ignore IOException if it occurs
    }
  }

  //? Method to create a folder in the plugin's data folder
  public static void createFolderInFolder(String fileName) {
    // Create a new File object representing the folder
    var file = new File(MotionCore.getInstance().getDataFolder(), fileName);

    // If the folder doesn't exist, create it and any necessary parent folders
    if (!file.exists()) file.mkdirs();
  }

  //? Method to create a folder in a specified directory
  public static void createFolderInFolder(String fileName, File folder) {
    // Create a new File object representing the folder
    var file = new File(folder, fileName);

    // If the folder doesn't exist, create it and any necessary parent folders
    if (!file.exists()) file.mkdirs();
  }


  //* - Functions to manage files in a more optimal way. -

  public static void deleteFolderInFolder(String folderName, String name) {
    var path = getFolder(folderName, MotionCore.getInstance().getDataFolder());
    var cinematic = getFolder(name, path);

    File[] files = cinematic.listFiles();
    if (files != null) for (File file : files) file.delete();

    cinematic.delete();

    Bukkit.getConsoleSender().sendMessage("" + path);
    Bukkit.getConsoleSender().sendMessage("" + getFolder(name, path));
    MotionCore.getInstance().saveConfig();
  }


  //? Method to get a folder in the plugin's data folder
  public static File getFolder(String folderName) {
    // Return a File object representing the specified folder in the plugin's data folder
    return new File(MotionCore.getInstance().getDataFolder(), folderName);
  }


  //? Method to get a folder in a specified directory
  public static File getFolder(String folderName, File root) {
    // Return a File object representing the specified folder in the specified directory
    return new File(root, folderName);
  }


  //? Method to get a YAML file in a specified folder
  public static File getFile(File dataFolder, String fileName) {
    // Return a File object representing the specified YAML file in the specified folder
    return new File(dataFolder, fileName.toLowerCase() + ".yml");
  }


  //? Method to load YAML configuration from a file
  public static YamlConfiguration getFileConfig(File file) {
    // Load YAML configuration from the specified file and return it
    return YamlConfiguration.loadConfiguration(file);
  }

}
