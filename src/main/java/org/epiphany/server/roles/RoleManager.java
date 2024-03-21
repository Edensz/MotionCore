package org.epiphany.server.roles;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.epiphany.api.Data;
import org.epiphany.utils.ChatUtils;

public class RoleManager {


  public void set(RoleCreator role, Player player) {
    var roleName = Data.get(player, "role", PersistentDataType.STRING);

    if (roleName == null || roleName.equals(role.name())) {
      ChatUtils.error("Ya perteneces al equipo insertado.", player);
      return;
    }


    Data.set(player, "role", PersistentDataType.STRING, role.name());
    setPriority(player);

    player.sendMessage(ChatUtils.prefix("Se te ha asignado el rol de&8: " + role.getTeamColor() + role.getDisplay() + "&8."));
    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,2);
  }


  private RoleCreator getRole(Player player) {
    return RoleCreator.valueOf(Data.get(player, "role", PersistentDataType.STRING));
  }

  private void setPriority(Player player) {
    var priority = getRole(player).getPriority().toString();
    var scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    var team = scoreboard.getTeam(priority);

    if (team == null) scoreboard.registerNewTeam(priority);
    else team.addEntry(player.getName());
  }


}
