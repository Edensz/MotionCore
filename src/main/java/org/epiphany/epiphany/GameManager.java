package org.epiphany.epiphany;

import org.epiphany.Epiphany;
import org.epiphany.api.FileApi;
import org.epiphany.epiphany.player.PlayerHandler;
import org.epiphany.utils.ChatUtils;


public class GameManager {


  public static void startServer() {
    var config = FileApi.getConfigYML();

    config.set("global.started", true);
    Epiphany.getInstance().saveConfig();
    PlayerHandler.flashAll();

  }



  public static void resetServer() {
    var config = FileApi.getConfigYML();

    config.set("global.started", false);
    config.set("global.day", 0);
    config.set("global.hour", 0);

    Epiphany.getInstance().saveConfig();
    PlayerHandler.kickAllPlayers(ChatUtils.font("#B14B4BPartida Reiniciada"));
  }


}
