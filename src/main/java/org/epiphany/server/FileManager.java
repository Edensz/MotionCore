package org.epiphany.server;

import org.bukkit.Bukkit;
import org.bukkit.persistence.PersistentDataType;
import org.epiphany.api.Data;
import org.epiphany.api.FileApi;

import java.io.IOException;

public class FileManager {


  public static void createPlayerFile(String username) {
    var playerFolder = FileApi.getFolder("PlayerData");

    FileApi.createYAMLFile(playerFolder, username);

    updatePlayerFile(username);
  }

  public static void updatePlayerFile(String username) {
    var file = FileApi.getFile(FileApi.getFolder("PlayerData"), username);
    var config = FileApi.getFileConfig(file);
    var player = Bukkit.getPlayer(username);

    var completion = Data.get(player, "completion", PersistentDataType.INTEGER);
    var deaths = Data.get(player, "deaths", PersistentDataType.INTEGER);
    var timePlayed = Data.get(player, "timePlayed", PersistentDataType.INTEGER);
    var magicLevel = Data.get(player, "magicLevel", PersistentDataType.INTEGER);
    var profileColor = Data.get(player, "profileColor", PersistentDataType.STRING);
    var profileGlint = Data.get(player, "profileGlint", PersistentDataType.BOOLEAN);


    if (completion == null) completion = 0;
    if (deaths == null) deaths = 0;
    if (timePlayed == null) timePlayed = 0;
    if (magicLevel == null) magicLevel = 0;
    if (profileColor == null) profileColor = "GRAY";
    if (profileGlint == null) profileGlint = false;

    config.set("display", username);
    config.set("completion", completion);
    config.set("deaths", deaths);
    config.set("timePlayed", timePlayed);
    config.set("magicLevel", magicLevel);
    config.set("profileColor", profileColor);
    config.set("profileGlint", profileGlint);

    try {config.save(file);}
    catch (IOException ignored) {}
  }


}
