package org.motion.player;

import org.motion.MotionCore;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.motion.utils.ChatUtils;


public class PlayerHandler {

  //* - Functions to manage players instantly. -

  //? Method to play a sound for a player at a specific location with a given pitch
  public static void playSound(Sound sound, float pitch, Player audience) {
    audience.playSound(audience.getLocation(), sound, 1, pitch);
  }

  //? Method to send an error message to the player and play an associated error sound
  public static void errorMessage(String text, Player audience) {
    // Send an error message to the player
    audience.sendMessage(ChatUtils.error(text));
    // Play an error sound for the player
    PlayerHandler.playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 0.76f, audience);
  }

  //? Method to send a message with a prefix to the player and play an associated sound
  public static void sendMessage(String text, Player audience) {
    // Send a message with a prefix to the player
    audience.sendMessage(ChatUtils.prefix(text));
    // Play an associated sound for the player
    PlayerHandler.playSound(Sound.ITEM_TRIDENT_RETURN, 0.76f, audience);
  }


  //* - Delayed functions to manage players. -

  //? Method to play a delayed sound for the player
  public static void playDelayedSound(Sound sound, float pitch, Player audience, int delay) {
    // Schedule a task to run after a specified delay
    Bukkit.getScheduler().runTaskLater(MotionCore.getInstance(), () -> {
      // Play the sound for the player
      PlayerHandler.playSound(sound, pitch, audience);
    }, delay);
  }

  //? Method to cast a delayed action bar for the player
  public static void sendDelayedActionBar(String text, Player player, int delay) {
    // Schedule a task to run after a specified delay
    Bukkit.getScheduler().runTaskLater(MotionCore.getInstance(), () -> {
      // Sends the action bar to the player
      player.sendActionBar(ChatUtils.format(text));
    }, delay);
  }

}
