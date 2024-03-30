package org.motion.tool;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.motion.MotionCore;
import org.motion.player.PlayerFileHelper;
import org.motion.utils.ChatUtils;
import org.motion.utils.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.motion.utils.PluginFileAPI;


@CommandAlias("cinematic")
@CommandPermission("admin.perm")
public class CinematicCommand extends BaseCommand {

  @Subcommand("panel")
  @Default
  private void panel(CommandSender commandSender) {
    if (!(commandSender instanceof Player sender)) return;

    new CinematicPanel(sender, CinematicPanel.PanelMode.VIEW).open();
  }

  @Subcommand("help")
  @CatchUnknown @Default
  private void help(CommandSender commandSender) {
    if (!(commandSender instanceof Player sender)) return;

    sender.sendMessage(ChatUtils.format("&7/#D56C6Ccinematic delete &7[#D56C6CCinematic&7]"));
    sender.sendMessage(ChatUtils.format("&7/#D56C6Ccinematic play &7[#D56C6CCinematic&7] &7[#D56C6CPlayer&7]"));
    sender.sendMessage(ChatUtils.format("&7/#D56C6Ccinematic stop &7[#D56C6CPlayer&7]"));
    sender.sendMessage(ChatUtils.format("&7/#D56C6Ccinematic finish &7[#D56C6CPlayer&7]"));
    sender.sendMessage(ChatUtils.format("&7/#D56C6Ccinematic create &7[#D56C6CName&7] &7[#D56C6CFPS&7] &7&o[#D56C6CType&7]"));

    PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.55f, sender);
    PlayerHandler.playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 0.95f, sender);
  }


  @Subcommand("delete")
  @CommandCompletion("@Cinematics")
  private void delete(CommandSender commandSender, String name) {
    PluginFileAPI.deleteFolderInFolder("cinematics", name);

    if (!(commandSender instanceof Player sender)) return;

    PlayerHandler.sendMessage("#9CCF9ELa cinemática fue eliminada correctamente.", sender);
    PlayerHandler.playSound(Sound.ITEM_SHIELD_BREAK,1.5F, sender);
  }

  @Subcommand("play")
  @CommandCompletion("@Cinematics")
  private void play(CommandSender commandSender, String name, @Optional @Flags("other") Player audience) {
    if (audience == null) {
      if (commandSender instanceof Player sender) audience = sender;
      else return;
    }

    if (!PlayerFileHelper.getStatusMode(audience, PlayerFileHelper.Status.CHILLING)) return;

    new CinematicManager(audience, name).play();

    if (!(commandSender instanceof Player sender)) return;

    PlayerHandler.sendMessage("#CFB39CReproduciendo la cinemática #D78E51" + name + "#CFB39C a #D78E51" + audience.getName() + "#CFB39C.", sender);
    PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP,1.35f, sender);
    PlayerHandler.playSound(Sound.BLOCK_PISTON_EXTEND,1.35f, sender);
  }


  @Subcommand("stop")
  private void stop(CommandSender commandSender, @Optional @Flags("other") Player audience) {
    if (audience == null) {
      if (commandSender instanceof Player sender) audience = sender;
      else return;
    }

    if (!PlayerFileHelper.getStatusMode(audience, PlayerFileHelper.Status.WATCHING)) return;

    PlayerFileHelper.updatePlayerFile(audience, PlayerFileHelper.Status.CHILLING, true);

    if (!(commandSender instanceof Player sender)) return;

    PlayerHandler.sendMessage("#CFB39CSe le ha cancelado la reproducción de la cinemática a #D78E51" + audience.getName() + ".", sender);
    PlayerHandler.playSound(Sound.BLOCK_BEACON_DEACTIVATE,1.35f, sender);
    PlayerHandler.playSound(Sound.BLOCK_PISTON_CONTRACT,1.35f, sender);
  }


  @Subcommand("finish")
  private void finish(CommandSender commandSender) {
    if (!(commandSender instanceof Player sender)) return;

    if (!PlayerFileHelper.getStatusMode(sender, PlayerFileHelper.Status.RECORDING)) {
      PlayerHandler.errorMessage("¡No estás grabando ninguna cinemática!", sender);
      return;
    }

    new CinematicManager(sender, null).finish();

    PlayerHandler.sendMessage("#85C47F¡La cinemática fue guardada correctamente!", sender);
    PlayerHandler.playSound(Sound.BLOCK_CHEST_CLOSE,1.25F, sender);
    PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP,1.85F, sender);
    PlayerHandler.playSound(Sound.UI_BUTTON_CLICK, 1.25f, sender);
  }


  @Subcommand("create")
  private void create(CommandSender commandSender, String cinematicName, int frameRate, @Optional CinematicHelper.CinematicType cinematicType) {
    if (!(commandSender instanceof Player sender)) return;

    if (cinematicType == null) cinematicType = CinematicHelper.CinematicType.BASIC;
    else if (cinematicType != CinematicHelper.CinematicType.BASIC) {
      switch (cinematicType) {
        case MOVIE -> new CinematicPanel(sender, CinematicPanel.PanelMode.MOVIE).open();
        case CHAIN -> new CinematicPanel(sender, CinematicPanel.PanelMode.CHAIN).open();
      }
      return;
    }

    if (!PlayerFileHelper.getStatusMode(sender, PlayerFileHelper.Status.CHILLING)) {
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

    final var finalCinematicType = cinematicType.name().toLowerCase();
    Bukkit.getScheduler().runTaskLater(MotionCore.getInstance(), () -> {
      new CinematicManager(sender, cinematicName).create(frameRate, finalCinematicType);
      PlayerHandler.playSound(Sound.BLOCK_CHEST_OPEN,1.25F, sender);
      PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.25f, sender);
      PlayerHandler.playSound(Sound.UI_BUTTON_CLICK, 1.25f, sender);
    }, 100);
  }

}
