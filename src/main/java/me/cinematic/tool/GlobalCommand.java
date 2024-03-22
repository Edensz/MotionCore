package me.cinematic.tool;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.cinematic.Cinematic;
import me.cinematic.player.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CommandAlias("cinematic")
@CommandPermission("admin.perm")
public class GlobalCommand extends BaseCommand {


  @Subcommand("play")
  private void play(CommandSender commandSender, String name) {
    if (!(commandSender instanceof Player sender)) return;

    new CinematicManager().play(name, sender);
  }


  @Subcommand("finish")
  private void finish(CommandSender commandSender) {
    if (!(commandSender instanceof Player sender)) return;

    if (!Cinematic.getInstance().getConfig().getBoolean("recording")) {
      PlayerHandler.errorMessage("No se está grabando ninguna cinemática en este instante.", sender);
      return;
    }


    new CinematicManager().finish(sender);
  }


  @Subcommand("record")
  private void record(CommandSender commandSender, String cinematicName, int frameRate) {
    if (!(commandSender instanceof Player sender)) return;

    if (Cinematic.getInstance().getConfig().getBoolean("recording")) {
      PlayerHandler.errorMessage("No puedes grabar dos cinemáticas a la vez.", sender);
      return;
    }


    PlayerHandler.sendMessage("#C65C5CCada movimiento que hagas será grabado para tu cinemática.", sender);

    PlayerHandler.sendDelayedMessage("Se iniciará la creación dentro de &l5 segundos&r.", sender, 20);
    PlayerHandler.playDelayedSound(Sound.UI_BUTTON_CLICK,1, sender, 40);
    PlayerHandler.playDelayedSound(Sound.UI_BUTTON_CLICK,0.9f, sender, 60);
    PlayerHandler.playDelayedSound(Sound.UI_BUTTON_CLICK,0.8f, sender, 80);
    PlayerHandler.playDelayedSound(Sound.UI_BUTTON_CLICK,0.7f, sender, 100);
    PlayerHandler.playDelayedSound(Sound.UI_BUTTON_CLICK,0.6f, sender, 120);

    Bukkit.getScheduler().runTaskLater(Cinematic.getInstance(), () -> new CinematicManager().create(cinematicName, sender, frameRate), 120);

  }


}
