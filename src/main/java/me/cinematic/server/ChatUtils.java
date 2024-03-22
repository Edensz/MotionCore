package me.cinematic.server;

import org.bukkit.ChatColor;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {
  public static String prefix(String text) {return format("&8&l[&r#A7A7A7Cinematic #868686Tool&8&l]&r &7» &f" + text);}
  public static String error(String text) {return format("&8&l[&r#B02A2AError&8&l]&r &7» #AB4D4D" + text);}


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


}

