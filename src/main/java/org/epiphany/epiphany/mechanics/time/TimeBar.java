package org.epiphany.epiphany.mechanics.time;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.epiphany.api.FileApi;
import org.epiphany.epiphany.GameHelper;
import org.epiphany.utils.ChatUtils;

public class TimeBar {
  private static BossBar bar;
  private static BossBar blankBar;
  private static BossBar infoBar;

  private static String title;
  public static int minutes = 0;
  public static int hour = TimeManager.getHour();
  public static int day = TimeManager.getDay();


  public static void create() {
    updateTitle();

    blankBar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
    bar = Bukkit.createBossBar("₪", BarColor.WHITE, BarStyle.SEGMENTED_6);
    infoBar = Bukkit.createBossBar(ChatUtils.format(title), BarColor.YELLOW, BarStyle.SOLID);

    tick(TimeManager.getHour());
  }


  public static void tick(double hour) {
    updateTitle();

    infoBar.setTitle(ChatUtils.format(title));
    bar.setProgress(hour/24);
  }


  public static void updateTitle() {
    title = "#B47359⌚ &7» #B47359Día&7: #B5A9A4" + day + " &7&l┇&r #B47359Hora&7: #B5A9A4" + hour + ":" + String.format("%02d", minutes) + " &7« #B47359⌚";
  }


  public static void add(Player player) {
    if (!GameHelper.isStarted()) return;

    blankBar.addPlayer(player);
    bar.addPlayer(player);
    infoBar.addPlayer(player);
  }

  public static void remove(Player player) {
    blankBar.removePlayer(player);
    bar.removePlayer(player);
    infoBar.removePlayer(player);
  }

  public static void removeAll() {
    blankBar.removeAll();
    bar.removeAll();
    infoBar.removeAll();
  }

}
