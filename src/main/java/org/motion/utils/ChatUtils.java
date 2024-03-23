package org.motion.utils;

import org.bukkit.ChatColor;

import java.util.regex.Pattern;


public class ChatUtils {
  public static String prefix(String text) {return format("&8&l[#797B8EMotionCore&8&l]&r &7» #A9AAB9" + text);}
  public static String error(String text) {return format("&8&l[&r#B02A2AError&8&l]&r &7» #AB4D4D" + text);}


  //? Customize Chat Color Formatting with Bungee API
  public static String format(String text) {
    // Define a pattern to match color codes in the format "#RRGGBB"
    var pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    // Replace occurrences of "#&" with "#" to prevent invalid color codes
    var message = text.replace("#&", "#");
    // Create a matcher to find color codes in the message
    var matcher = pattern.matcher(message);

    // Iterate through each match found by the matcher
    while (matcher.find()) {
      // Extract the color code substring
      var code = message.substring(matcher.start(), matcher.end());
      // Replace the color code substring with the corresponding ChatColor
      message = message.replace(code, "" + net.md_5.bungee.api.ChatColor.of(code));
      // Reset the matcher to search for more color codes in the updated message
      matcher = pattern.matcher(message);
    }

    // Translate '&' color codes to Minecraft's ChatColor format
    return ChatColor.translateAlternateColorCodes('&', message);
  }

}

