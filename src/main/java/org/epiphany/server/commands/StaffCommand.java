package org.epiphany.server.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.epiphany.Epiphany;
import org.epiphany.api.FileApi;
import org.epiphany.epiphany.GameManager;
import org.epiphany.epiphany.mechanics.time.DayCreator;
import org.epiphany.epiphany.mechanics.time.TimeManager;
import org.epiphany.epiphany.player.GlobalNotifier;
import org.epiphany.epiphany.player.PlayerHandler;
import org.epiphany.server.roles.RoleCreator;
import org.epiphany.server.roles.RoleManager;
import org.epiphany.utils.ChatUtils;


@CommandAlias("staff")
@CommandPermission("admin.perm")
public class StaffCommand extends BaseCommand {



  @Subcommand("server")
  @CommandCompletion("start|reset")
  private void server(CommandSender commandSender, String action) {
    if (!(commandSender instanceof Player sender)) return;

    var config = FileApi.getConfigYML();
    if (!sender.getName().equals(config.get("global.maxAuthority"))) {
      ChatUtils.error("Este comando solo puede ser ejecutado por la máxima autoridad del servidor, para modificarla debes entrar al explorador de archivos de tu panel de control, dirigirte al archivo config.yml, ubicado en Files / plugins / Epiphany / config.yml y modificar la opción que diga maxAuthority a tu nombre de usuario en Minecraft. ", sender);
      return;
    }


    switch (action) {
      // Starts the Epiphany server only if it hasn't been started yet
      case "start" -> {
        if (config.getBoolean("global.started")) {
          ChatUtils.error("El servidor ya ha iniciado.", sender);
          return;
        }

        GameManager.startServer();
      }

      // Resets the Epiphany server only if it has already started
      case "reset" -> {
        if (!config.getBoolean("global.started")) {
          ChatUtils.error("No puedes reiniciar el servidor si este aún no ha iniciado.", sender);
          return;
        }

        PlayerHandler.freezeAll(true);
        GlobalNotifier.broadcast("Un@ operador@ ha #8462B4reiniciado la partida&8.");
        GlobalNotifier.broadcast("#B14B4BEl servidor se cerrara en 10 segundos&8.", false);
        GlobalNotifier.playSound(Sound.ENTITY_BLAZE_DEATH, 0.87f);

        Bukkit.getScheduler().runTaskLater(Epiphany.getInstance(), GameManager::resetServer, 200);
      }
    }
  }



  @Subcommand("setFreeze")
  private void setFreeze(CommandSender commandSender, @Flags("others") Player player, boolean action) {
    if (!(commandSender instanceof Player sender)) return;

    var formattedAction = "congelado";
    if (action) formattedAction = "descongelado";

    PlayerHandler.freeze(player, action);
    ChatUtils.staff("&7Se ha " + formattedAction + " a&8: #945EA9" + player.getName() + "&8.", sender);
  }



  @Subcommand("setDay")
  private void setDay(CommandSender commandSender, int day) {
    if (!(commandSender instanceof Player sender)) return;

    if (day > DayCreator.toList().size() || day == 0 || day == TimeManager.getDay()) {
      ChatUtils.error("Debes ingresar un día válido.", sender);
      return;
    }

    TimeManager.elapseDay(day);
    ChatUtils.staff("&7Nos hemos movido al día&8: #945EA9" + day + "&8.", sender);
  }



  @Subcommand("setHour")
  private void setHour(CommandSender commandSender, int hour) {
    if (!(commandSender instanceof Player sender)) return;

    TimeManager.elapseHour(hour);
    ChatUtils.staff("&7La hora ha sido modificada a&8: #945EA9" + hour + "&8.", sender);
  }



  @Subcommand("setRole")
  private void setTeam(CommandSender commandSender, RoleCreator role, @Flags("others") Player player) {
    if (!(commandSender instanceof Player sender)) return;

    new RoleManager().set(role, player);
    sender.playSound(sender.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1.5f);
  }



}
