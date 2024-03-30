package org.motion.tool;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.motion.utils.ItemBuilder;
import org.motion.utils.PluginFileAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class CinematicHelper {
  public static final File cinematicsFolder = PluginFileAPI.getFolder("cinematics");

  @Nullable
  public static ArrayList<String> cinematicListAsArray() {
    var cinematicList = cinematicsFolder.list();
    if (cinematicList == null) return null;

    return new ArrayList<>(Arrays.asList(cinematicList));
  }


  protected enum CinematicType {BASIC, MOVIE, CHAIN}

  @Nullable
  protected static ItemStack setCinematicIcon(String cinematicName) {
    final var cinematic = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
    final var cinematicProperties = PluginFileAPI.getFile(cinematic, "properties");
    final var cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);

    final var cinematicType = cinematicConfig.getString("properties.type");
    if (cinematicType == null) return null;

    final var cinematicInfoBaseColor = getCinematicInfoColor(cinematicType, true);
    final var cinematicInfoLightColor = getCinematicInfoColor(cinematicType, false);

    return new ItemBuilder(getCinematicIconMaterial(cinematicType)).setDisplayName("&7• " + cinematicInfoBaseColor + cinematicName + " &7•").setLore(
            "",
            "&7•&r " + cinematicInfoBaseColor + "Nombre del Autor&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.author"),
            "&7•&r " + cinematicInfoBaseColor + "Duración Total&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.totalDuration"),
            "&7•&r " + cinematicInfoBaseColor + "Cuadros por Segundo&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.frameRate"),
            "&7•&r " + cinematicInfoBaseColor + "Fotogramas grabados&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.framesRecorded"),
            ""
    ).setGlint().setID(cinematicName).build();
  }

  protected static Material getCinematicIconMaterial(@NotNull String cinematicType) {
    //Default material in case of an error.
    var cinematicIconMaterial = Material.BARRIER;

    switch (cinematicType) {
      case "chain" -> cinematicIconMaterial = Material.WARPED_SIGN;
      case "basic" -> cinematicIconMaterial = Material.OAK_SIGN;
      case "movie" -> cinematicIconMaterial = Material.DARK_OAK_SIGN;
    }

    return cinematicIconMaterial;
  }

  protected static String getCinematicInfoColor(@NotNull String cinematicType, boolean baseColor) {
    //Default colors in case of any error.
    var cinematicIconBaseColor = "&5";
    var cinematicIconLightColor = "&5";

    switch (cinematicType) {
      case "chain" -> {
        cinematicIconBaseColor = "#6F8CBB";
        cinematicIconLightColor = "#94A5C1";
      }
      case "basic" -> {
        cinematicIconBaseColor = "#D2915F";
        cinematicIconLightColor = "#D5A885";
      }
      case "movie" -> {
        cinematicIconBaseColor = "#ADADAD";
        cinematicIconLightColor = "#CBCBCB";
      }
    }

    if (baseColor) return cinematicIconBaseColor;
    else return cinematicIconLightColor;
  }


  protected static double roundToNearestTenth(double number) {
    double factor = Math.pow(10, 1);
    return Math.round(number * factor) / factor;
  }


  public static boolean doesCinematicExist(String cinematicToIndex) {
    var cinematicList = cinematicsFolder.list();
    if (cinematicList == null) return false;
    for (String cinematic : cinematicList) if (cinematicToIndex.equals(cinematic)) return true;
    return false;
  }

}