package org.epiphany.server.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.epiphany.server.menus.global.PersonalProfile;
import org.epiphany.server.menus.global.MemberProfiles;
import org.epiphany.server.menus.global.Calendar;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CommandAlias("epiphany")
public class GlobalCommand extends BaseCommand {


  @Subcommand("calendar")
  private void calendar(CommandSender commandSender) {
    if (!(commandSender instanceof Player sender)) return;

    new Calendar(sender).open();

    sender.playSound(sender.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1.55f);
  }

  @Subcommand("profile")
  private void profile(CommandSender commandSender) {
    if (!(commandSender instanceof Player sender)) return;

    new PersonalProfile(sender, sender.getName().toLowerCase()).open();

    sender.playSound(sender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.55f);
  }

  @Subcommand("members")
  private void members(CommandSender commandSender) {
    if (!(commandSender instanceof Player sender)) return;

    new MemberProfiles(sender).open();

    sender.playSound(sender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.55f);
  }


}
