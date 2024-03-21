package org.epiphany.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.epiphany.Epiphany;
import org.epiphany.api.FileApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GlobalUtils {

  public static void debugMessage(String message) {
    Bukkit.getConsoleSender().sendMessage("DEBUG: " + message);
  }

  //TODO Poner ChallengeCreator.values().length
  public static String completionPercentage(int completion) {
    return completion/ 5 * 100 + "%";
  }

  public static String formatSeconds(int seconds) {
    var hours = seconds / 3600;
    var minutes = (seconds % 3600) / 60;
    var remainingSeconds = seconds % 60;

    return String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
  }



  public static void spawnMythicMob(String id, Location location) {
    MythicMob mob;
    mob = MythicBukkit.inst().getMobManager().getMythicMob(id).orElse(null);

    if (mob == null) return;
    mob.spawn(BukkitAdapter.adapt(location), 1);
  }



  public static void pasteSchem(String filename, World world, int X, int Y, int Z) {
    var file = new File(FileApi.getDataFolder().getAbsolutePath() + "/schematics/" + filename + ".schem");
    var format = ClipboardFormats.findByFile(file);

    if (format == null) return;


    try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
      var clipboard = reader.read();

      try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(world),-1)) {
        var operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(X, Y, Z)).copyEntities(true).ignoreAirBlocks(true).build();

        try {
          Operations.complete(operation);
          editSession.close();
        }
        catch (WorldEditException ignored) {}
      }
    }
    catch (IOException ignored) {}
  }


}
