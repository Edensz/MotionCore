package org.epiphany.server;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.epiphany.api.FileApi;
import org.epiphany.api.Registrable;
import org.epiphany.epiphany.GameHelper;
import org.epiphany.epiphany.mechanics.time.TimeBar;
import org.epiphany.epiphany.player.GlobalNotifier;
import org.epiphany.epiphany.player.MemberList;
import org.epiphany.utils.ChatUtils;
import org.epiphany.utils.DataUtils;


@Registrable
public class ServerListener implements Listener {



  @EventHandler
  private void chat(AsyncPlayerChatEvent event) {
    var message = event.getMessage();
    var player = event.getPlayer();
    var format = ServerHelper.formatPlayerName(player, ServerHelper.FormatFor.CHAT);

    event.setFormat(ChatUtils.format(format) + message);
  }



  @EventHandler
  private void ping(ServerListPingEvent event) {
    var config = FileApi.getConfigYML();
    var topper = config.getString("motd.topper");
    var footer = config.getString("motd.footer");

    event.setMotd(ChatUtils.format(topper + "\n" + footer));
  }



  @EventHandler
  private void join(PlayerJoinEvent event) {
    var player = event.getPlayer();
    var name = player.getName();
    var whitelist = MemberList.toList();


    for (int i = 0; i < whitelist.size(); i++) {
      var registerName = whitelist.get(i).getName;

      if (name.equalsIgnoreCase(registerName) || player.isOp()) break;

      if (i == (whitelist.size() - 1)) player.banPlayer("&cNo perteneces a la lista de miembros.");
    }


    TimeBar.add(player);
    GameHelper.teleportToWaitroom();
    DataUtils.initializeData(player);
    GlobalNotifier.playSound(Sound.BLOCK_NOTE_BLOCK_BELL, 2);

    event.setJoinMessage(ChatUtils.font("#875EA9" + name + " #906CA0se ha conectado."));
  }



  @EventHandler
  private void quit(PlayerQuitEvent event) {
    var name = event.getPlayer().getName();

    GlobalNotifier.playSound(Sound.BLOCK_NOTE_BLOCK_BELL, 1.65f);

    event.setQuitMessage(ChatUtils.font("#875EA9" + name + " #906CA0se ha desconectado."));
  }



}
