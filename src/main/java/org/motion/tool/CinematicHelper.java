package org.motion.tool;

import org.motion.utils.PluginFileAPI;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


public class CinematicHelper {
  protected static final File cinematicsFolder = PluginFileAPI.getFolder("cinematics");

  public static double roundTo(double number, int decimals) {
    double factor = Math.pow(10, decimals);
    return Math.round(number * factor) / factor;
  }

}
