package org.epiphany.epiphany.player;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.epiphany.Epiphany;
import org.epiphany.utils.ChatUtils;


// * The GlobalNotifier class provides methods for sending messages, titles, and playing sounds to all online players.
public class GlobalNotifier {


  // Sends an action bar with the specified text to all online players.
  public static void sendAction(String text) {
    for (Player each : Bukkit.getOnlinePlayers()) each.sendActionBar(ChatUtils.format(text));
  }

  // Sends a title with the specified title and subtitle to all online players.
  public static void sendTitle(String title, String subtitle) {
    for (Player each : Bukkit.getOnlinePlayers()) each.sendTitle(ChatUtils.format(title), ChatUtils.format(subtitle), 15, 50, 40);
  }
  public static void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
    for (Player each : Bukkit.getOnlinePlayers()) each.sendTitle(ChatUtils.format(title), ChatUtils.format(subtitle), fadeIn, stay, fadeOut);
  }
  public static void sendTitle(String title, int fadeIn, int stay, int fadeOut) {
    for (Player each : Bukkit.getOnlinePlayers()) each.sendTitle(ChatUtils.format(title), "", fadeIn, stay, fadeOut);
  }

  // Broadcasts a message to all online players.
  public static void broadcast(String text) {
    for (Player each : Bukkit.getOnlinePlayers()) {
      each.sendMessage(ChatUtils.prefix("#9993A3" + text));
      each.playSound(each.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1.45f);
    }
  }
  public static void broadcast(double text) {
    for (Player each : Bukkit.getOnlinePlayers()) each.sendMessage(ChatUtils.prefix(String.valueOf(text)));
  }
  public static void broadcast(String text, boolean prefix) {
    for (Player each : Bukkit.getOnlinePlayers()) if (!prefix) each.sendMessage(ChatUtils.format(String.valueOf(text)));
  }

  // Plays a sound to all online players.
  public static void playSound(Sound sound, float pitch) {
    for (Player each : Bukkit.getOnlinePlayers()) each.playSound(each.getLocation(), sound, 1, pitch);
  }
  public static void playSound(String sound, float pitch) {
    for (Player each : Bukkit.getOnlinePlayers()) each.playSound(each.getLocation(), sound, 1, pitch);
  }


  // Plays a sound with a delay to all online players.
  public static void playDelayedSound(Sound sound, float pitch, int delay) {
    for (Player each : Bukkit.getOnlinePlayers()) Bukkit.getScheduler().runTaskLater(Epiphany.getInstance(), () -> each.playSound(each.getLocation(), sound, 1, pitch), delay);
  }
  public static void playDelayedSound(String sound, float pitch, int delay) {
    for (Player each : Bukkit.getOnlinePlayers()) Bukkit.getScheduler().runTaskLater(Epiphany.getInstance(), () -> each.playSound(each.getLocation(), sound, 1, pitch), delay);
  }


}
