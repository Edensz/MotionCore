package org.epiphany.epiphany.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.epiphany.api.FileApi;

import java.util.*;

public enum MemberList {

  Iceflaked(),
  tatsushiri(),
  Litrowo(),
  zLofro(),
  MeteorCry(),
  CarmenMomazer(),
  AleSuarez(),
  TheBoyPanda(),
  FaboVCF(),
  Mario03_(),
  Azgalord_(),
  CharlyDavid04(),
  zapata2013(),

  ;

  public final OfflinePlayer getPlayer;
  public final String getName;
  public final UUID getUUID;
  public final int getTimePlayed;
  public final int getMagicLevel;

  MemberList() {
    this.getName = name().toLowerCase();
    this.getPlayer = Bukkit.getOfflinePlayer(getName);
    this.getUUID = Objects.requireNonNull(getPlayer).getUniqueId();
    this.getTimePlayed = getConfigValue(getName, "timePlayed");
    this.getMagicLevel = getConfigValue(getName, "magicLevel");
  }


  private static int getConfigValue(String fileName, String value) {
    return FileApi.getFileConfig(FileApi.getFile(FileApi.getDataFolder(), fileName)).getInt(value);
  }

  public static List<MemberList> toList() {
    return Arrays.stream(values()).sorted(Comparator.comparingInt(player -> player.getTimePlayed)).toList();
  }


}
