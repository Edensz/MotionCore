package me.cinematic.player;

import me.cinematic.Cinematic;
import me.cinematic.server.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlayerHandler {


  public static void playSound(Sound sound, float pitch, Player audience) {
    audience.playSound(audience.getLocation(), sound, 1, pitch);
  }

  public static void errorMessage(String text, Player audience) {
    audience.sendMessage(ChatUtils.error(text));
    PlayerHandler.playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 0.76f, audience);
  }

  public static void sendMessage(String text, Player audience) {
    audience.sendMessage(ChatUtils.prefix(text));
    PlayerHandler.playSound(Sound.ITEM_TRIDENT_RETURN, 0.76f, audience);
  }


  public static void sendDelayedMessage(String text, Player audience, int delay) {
    Bukkit.getScheduler().runTaskLater(Cinematic.getInstance(), () -> PlayerHandler.sendMessage(text, audience), delay);
  }

  public static void playDelayedSound(Sound sound, float pitch, Player audience, int delay) {
    Bukkit.getScheduler().runTaskLater(Cinematic.getInstance(), () -> PlayerHandler.playSound(sound, pitch, audience), delay);
  }


}
