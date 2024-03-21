package org.epiphany.utils;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.epiphany.api.Data;
import org.epiphany.server.menus.global.PersonalProfile;
import org.epiphany.server.roles.RoleCreator;

public class DataUtils {


  public static void initializeData(Player player) {

    if (isBooleanDataNull(player, "freeze")) Data.set(player, "freeze", PersistentDataType.BOOLEAN, false);
    if (isBooleanDataNull(player, "profileGlint")) Data.set(player, "profileGlint", PersistentDataType.BOOLEAN, false);
    if (isBooleanDataNull(player, "messageCooldown")) Data.set(player, "messageCooldown", PersistentDataType.BOOLEAN, false);
    if (isStringDataNull(player, "role")) Data.set(player, "role", PersistentDataType.STRING, RoleCreator.MEMBER.name());
    if (isStringDataNull(player, "profileColor")) Data.set(player, "profileColor", PersistentDataType.STRING, PersonalProfile.ProfileColor.GRAY.name());
    if (isIntegerDataNull(player, "timePlayed")) Data.set(player, "timePlayed", PersistentDataType.INTEGER, 0);
    if (isIntegerDataNull(player, "completion")) Data.set(player, "completion", PersistentDataType.INTEGER, 0);
    if (isIntegerDataNull(player, "magicLevel")) Data.set(player, "magicLevel", PersistentDataType.INTEGER, 0);
    if (isIntegerDataNull(player, "deaths")) Data.set(player, "deaths", PersistentDataType.INTEGER, 0);

  }


  private static boolean isBooleanDataNull(Player player, String key) {return Data.get(player, key, PersistentDataType.BOOLEAN) == null;}
  private static boolean isIntegerDataNull(Player player, String key) {return Data.get(player, key, PersistentDataType.INTEGER) == null;}
  private static boolean isStringDataNull(Player player, String key) {return Data.get(player, key, PersistentDataType.STRING) == null;}


}
