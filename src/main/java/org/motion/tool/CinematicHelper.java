package org.motion.tool;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.motion.utils.PluginFileAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class CinematicHelper {
  public enum CinematicType {BASIC, MOVIE, CHAIN}
  public static final File cinematicsFolder = PluginFileAPI.getFolder("cinematics");

  @Nullable
  public static ArrayList<String> cinematicListAsArray() {
    var cinematicList = cinematicsFolder.list();
    if (cinematicList == null) return null;
    return new ArrayList<>(Arrays.asList(cinematicList));
  }

  public static boolean doesCinematicExist(String cinematicName) {
    var cinematicList = cinematicsFolder.list();
    if (cinematicList == null) return false;
    for (String cinematic : cinematicList) if (cinematicName.equals(cinematic)) return true;
    return false;
  }

  @Nullable
  public static CinematicType getCinematicType(String cinematicName) {
    var cinematic = PluginFileAPI.getFolder(cinematicName, cinematicsFolder);
    var cinematicProperties = PluginFileAPI.getFile(cinematic, "properties");
    var cinematicType = PluginFileAPI.getFileConfig(cinematicProperties).getString("properties.type");
    if (cinematicType == null) return null;
    return CinematicType.valueOf(cinematicType.toUpperCase());
  }

  public static String getSpanishModeLabel(@NotNull String mode) {
    return switch (mode) {
      case "true" -> "Activado";
      case "false" -> "Desactivado";
      default -> mode;
    };
  }

  public static double roundToNearestTenth(double number) {
    final var factor = Math.pow(10, 1);
    return Math.round(number * factor) / factor;
  }

  public static void displayTransitionScreen(@NotNull Player player) {player.sendTitle("㊭", "", 35, 20, 35);}

}