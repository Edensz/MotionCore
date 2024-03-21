package org.epiphany.server;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.epiphany.Epiphany;
import org.epiphany.utils.ChatUtils;

public class ServerTablist {
  private static String star = "Error: Star Animation";
  private static String dot = "Error: Dot Animation";


  public static void display() {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(Epiphany.getInstance(), () -> {
      for (Player each : Bukkit.getOnlinePlayers()) {
        var onlinePlayers = Bukkit.getOnlinePlayers().size();
        var formatNames = ServerHelper.formatPlayerName(each, ServerHelper.FormatFor.TABLIST);

        each.setPlayerListHeader(ChatUtils.format("#D26284&k||&r " + star + ChatUtils.font(" #AE70C0&lEpiphany Project ") + star + " #D26284&k||&r"));
        each.setPlayerListName(ChatUtils.format(formatNames));
        each.setPlayerListFooter(ChatUtils.format(dot + " #AE70C0Jugadores Conectados&8: #D26284" + onlinePlayers + " " + dot));

      }
    }, 0, 1);
  }

  public static void animate() {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(Epiphany.getInstance(), () -> {
      for (int i = 0; i < 13; i++) {
        var finalI = i;

        Bukkit.getScheduler().runTaskLater(Epiphany.getInstance(), () -> {
          star = ServerHelper.setColorAnimation(finalI) + "★";
          dot = ServerHelper.setColorAnimation(finalI) + "●";
        }, 5 * i);
      }

    }, 0, 65);
  }


}
