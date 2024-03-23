package org.motion.tool;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Flags;
import co.aikar.commands.annotation.Subcommand;
import org.motion.MotionCore;
import org.motion.utils.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.motion.utils.PluginFileAPI;

import java.io.File;


@CommandAlias("cinematic")
@CommandPermission("admin.perm")
public class GlobalCommand extends BaseCommand {

  @Subcommand("delete")
  private void delete(CommandSender commandSender, String name) {
    PluginFileAPI.deleteFolderInFolder("cinematics", name);

    if (!(commandSender instanceof Player sender)) return;

    PlayerHandler.sendMessage("#9CCF9ELa cinemática fue eliminada correctamente.", sender);
    PlayerHandler.playSound(Sound.ITEM_SHIELD_BREAK,1.5F, sender);
  }


  @Subcommand("play")
  private void play(CommandSender commandSender, String name, @Flags("others") Player audience) {
    new CinematicManager().play(name, audience);

    if (!(commandSender instanceof Player sender)) return;

    PlayerHandler.sendMessage("#CFB39CReproduciendo la cinemática #D78E51" + name + "#CFB39C a #D78E51" + audience.getName() + "#CFB39C.", sender);
    PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP,1.35f, sender);
    PlayerHandler.playSound(Sound.BLOCK_PISTON_CONTRACT,1.35f, sender);
  }


  @Subcommand("finish")
  private void finish(CommandSender commandSender) {
    if (!(commandSender instanceof Player sender)) return;
    if (!MotionCore.getInstance().getConfig().getBoolean("recording")) {
      PlayerHandler.errorMessage("¡No se está grabando ninguna cinemática!", sender);
      return;
    }

    new CinematicManager().finish(sender);

    PlayerHandler.sendMessage("#85C47F¡La cinemática fue guardada correctamente!", sender);
    PlayerHandler.playSound(Sound.BLOCK_CHEST_CLOSE,0.85F, sender);
    PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP,1.85F, sender);
    PlayerHandler.playSound(Sound.UI_BUTTON_CLICK, 1.25f, sender);
  }


  @Subcommand("record")
  private void record(CommandSender commandSender, String cinematicName, int frameRate) {
    if (!(commandSender instanceof Player sender)) return;

    if (MotionCore.getInstance().getConfig().getBoolean("recording")) {
      PlayerHandler.errorMessage("¡No puedes grabar dos cinemáticas al mismo tiempo!", sender);
      return;
    }

    PlayerHandler.sendMessage("#C4857FSe grabará cada movimiento que hagas una vez que el contador termine.", sender);
    PlayerHandler.playSound(Sound.BLOCK_BELL_USE, 1.1f, sender);

    for (int countdown = 0; countdown < 5; countdown++) {
      var pitch = (float) (1 - (0.05 * countdown));
      var delay = countdown * 20;

      PlayerHandler.sendDelayedActionBar("&8• &7Empezando en &f" + (5 - countdown) + "&r &8•", sender, delay);
      PlayerHandler.playDelayedSound(Sound.BLOCK_AMETHYST_BLOCK_PLACE, pitch , sender, delay);
      PlayerHandler.playDelayedSound(Sound.UI_BUTTON_CLICK, pitch, sender, delay);
    }

    Bukkit.getScheduler().runTaskLater(MotionCore.getInstance(), () -> {
      new CinematicManager().create(cinematicName, sender, frameRate);
      PlayerHandler.playSound(Sound.BLOCK_CHEST_OPEN,0.85F, sender);
      PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.25f, sender);
      PlayerHandler.playSound(Sound.UI_BUTTON_CLICK, 1.25f, sender);
    }, 100);
  }

}
