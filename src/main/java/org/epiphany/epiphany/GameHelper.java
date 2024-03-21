package org.epiphany.epiphany;

import org.bukkit.potion.PotionEffectType;
import org.epiphany.api.FileApi;
import org.epiphany.epiphany.player.PlayerHandler;

public class GameHelper {


  public static void teleportToWaitroom() {
    PlayerHandler.freezeAll(false);
    PlayerHandler.addEffectToAll(PotionEffectType.REGENERATION, 1);
    PlayerHandler.addEffectToAll(PotionEffectType.SATURATION, 0);
    PlayerHandler.addEffectToAll(PotionEffectType.DAMAGE_RESISTANCE, 4);
  }


  public static boolean isStarted() {return FileApi.getConfigYML().getBoolean("global.started");}


}
