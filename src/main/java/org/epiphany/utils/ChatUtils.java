package org.epiphany.utils;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {

  public static String prefix(String text) {return format("&8&l[&r#A160B2" + font("Epiphany") + "&8&l]&r" + " &7» #9D8E9E" + text);}
  public static String staff(String text) {return format("&8&l[&r#E55C3B" + font("Staff") + "&8&l]&r &7» &r" + text);}
  public static String error(String text) {return format("&8&l[&r#B02A2A"  + font("Error") + "&8&l]&r &7» #AB4D4D" + text);}

  public static void error(String text, Player player) {
    player.sendMessage(error(text));
    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT,1, 0.76f);
  }
  public static void staff(String text, Player player) {
    player.sendMessage(staff(text));
    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1, 1.56f);
  }


  private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

  public static String format(String text) {
    text = text.replace("#&", "#");
    Matcher m = pattern.matcher(text);

    while (m.find()) {
      String cl = text.substring(m.start(), m.end());
      text = text.replace(cl, "" + net.md_5.bungee.api.ChatColor.of(cl));
      m = pattern.matcher(text);
    }
    return ChatColor.translateAlternateColorCodes('&', text);
  }


  public static String font(String text) {
    var formattedText = ChatUtils.format(text);

    formattedText = formattedText.replace("a", "ᴀ");
    formattedText = formattedText.replace("b", "ʙ");
    formattedText = formattedText.replace("c", "ᴄ");
    formattedText = formattedText.replace("d", "ᴅ");
    formattedText = formattedText.replace("e", "ᴇ");
    formattedText = formattedText.replace("f", "ғ");
    formattedText = formattedText.replace("g", "ɢ");
    formattedText = formattedText.replace("h", "ʜ");
    formattedText = formattedText.replace("i", "ɪ");
    formattedText = formattedText.replace("j", "ᴊ");
    formattedText = formattedText.replace("k", "ᴋ");
    formattedText = formattedText.replace("l", "ʟ");
    formattedText = formattedText.replace("m", "ᴍ");
    formattedText = formattedText.replace("n", "ɴ");
    formattedText = formattedText.replace("o", "ᴏ");
    formattedText = formattedText.replace("p", "ᴘ");
    formattedText = formattedText.replace("q", "ǫ");
    formattedText = formattedText.replace("r", "ʀ");
    formattedText = formattedText.replace("t", "ᴛ");
    formattedText = formattedText.replace("u", "ᴜ");
    formattedText = formattedText.replace("v", "ᴠ");
    formattedText = formattedText.replace("w", "ᴡ");
    formattedText = formattedText.replace("y", "ʏ");
    formattedText = formattedText.replace("z", "ᴢ");

    return formattedText;
  }


}

