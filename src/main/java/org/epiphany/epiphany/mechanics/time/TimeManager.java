package org.epiphany.epiphany.mechanics.time;

import org.bukkit.Bukkit;
import org.epiphany.Epiphany;
import org.epiphany.api.FileApi;
import org.epiphany.server.events.DayElapseEvent;
import org.epiphany.server.events.HourlyTickEvent;


public class TimeManager {


  //? Method to elapse an hour
  public static void elapseHour(int hour) {
    final var previousHour = getHour();
    if (getHour() == hour) return;

    if (hour == 24) {
      elapseDay(getDay() + 1);
      TimeBar.tick(0);
      return;
    }


    setHour(hour);

    TimeBar.tick(hour);
    Epiphany.getInstance().saveConfig();
    Bukkit.getServer().getPluginManager().callEvent(new HourlyTickEvent(getHour(), previousHour));
  }


  //? Method to elapse a day
  public static void elapseDay(int newDay) {
    final var previousDay = getDay();
    final var previousHour = getHour();
    if (previousDay == newDay || newDay > DayCreator.toList().size()) return;

    if (previousHour != 23) {
      TimeBar.day = newDay;
      TimeBar.tick(getHour());
    }


    setDay(newDay);
    elapseHour(0);

    TimeBar.tick(0);
    Epiphany.getInstance().saveConfig();
    Bukkit.getServer().getPluginManager().callEvent(new DayElapseEvent(newDay, previousDay));
  }


  //? Method to get the current day from the configuration
  public static int getDay() {return FileApi.getConfigYML().getInt("global.day");}

  //? Method to get the current hour from the configuration
  public static int getHour() {return FileApi.getConfigYML().getInt("global.hour");}

  //? Method to set the day in the configuration
  private static void setDay(int day) {FileApi.getConfigYML().set("global.day", day);}

  //? Method to set the hour in the configuration
  private static void setHour(int hour) {FileApi.getConfigYML().set("global.hour", hour);}


}
