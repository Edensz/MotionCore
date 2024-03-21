package org.epiphany.server;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.epiphany.api.Data;
import org.epiphany.server.roles.RoleCreator;
import org.epiphany.utils.ChatUtils;

public class ServerHelper {


  protected enum FormatFor{TABLIST,CHAT}
  protected static String formatPlayerName(Player player, FormatFor format) {
    var role = RoleCreator.valueOf(Data.get(player, "role", PersistentDataType.STRING));
    var name = ChatUtils.font(player.getName());

    return switch (format) {
      case TABLIST -> " " + role.getLogo() + " &7&l┇&r " + role.getTeamColor() + name + " ";
      case CHAT -> role.getLogo() + " &7&l┇&r " + role.getTeamColor() + name + " &7» " + role.getChatColor();
    };

  }

  protected static String setColorAnimation(int value) {

    return switch (value) {
      case 0, 12 -> "#D0D0D0";
      case 1, 11 -> "#CFCAC3";
      case 2, 10 -> "#D0C5B5";
      case 3, 9 -> "#CFBDA3";
      case 4, 8 -> "#CEB592";
      case 5, 7 -> "#CDB086";
      case 6 -> "#CCAC7E";

      default -> "Error: Out of bounds";
    };

  }


}
