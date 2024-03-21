package org.epiphany.epiphany.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.epiphany.Epiphany;
import org.epiphany.api.Data;
import org.epiphany.epiphany.GameHelper;
import org.epiphany.server.FileManager;
import org.epiphany.utils.ChatUtils;

public class PlayerTask {


  public static void run() {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(Epiphany.getInstance(), () -> {
      for (Player each : Bukkit.getOnlinePlayers()) {

        //? Increments the player time played data every second only if the player is online.
        var timePlayed = Data.get(each, "timePlayed", PersistentDataType.INTEGER);
        if (timePlayed == null) return;

        Data.set(each, "timePlayed", PersistentDataType.INTEGER, timePlayed + 1);
        FileManager.updatePlayerFile(each.getName());


        //? Displays an action bar if the game is not started yet
        var onlinePlayers = Bukkit.getOnlinePlayers().size();
        var maxPlayers = MemberList.toList().size();

        if (GameHelper.isStarted()) return;

        GlobalNotifier.sendAction(ChatUtils.font("#8462B4" + onlinePlayers + "&8/#8462B4" + maxPlayers + "#9993A3 Miembros conectados&8, #8462B4Modo&8: #9993A3Normal"));

      }
    }, 0, 20);
  }


}
