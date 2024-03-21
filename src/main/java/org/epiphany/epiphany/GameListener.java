package org.epiphany.epiphany;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.epiphany.Epiphany;
import org.epiphany.api.Registrable;
import org.epiphany.server.events.DayElapseEvent;
import org.epiphany.server.events.HourlyTickEvent;
import org.epiphany.epiphany.mechanics.time.DayCreator;
import org.epiphany.epiphany.mechanics.time.TimeBar;
import org.epiphany.epiphany.mechanics.time.TimeManager;
import org.epiphany.epiphany.player.GlobalNotifier;
import org.epiphany.utils.ChatUtils;


@Registrable
public class GameListener implements Listener {



  @EventHandler
  private void dayElapse(DayElapseEvent event) {
    // Get the list of days from DayCreator
    var dayList = DayCreator.toList();
    // Get the new day from the event
    var day = dayList.get(event.getNewDay() - 1);

    // If the day is null, return
    if (day == null) return;

    // Get the display name and value of the day
    var dayDisplay = day.getDisplay();
    var dayValue = day.getValue();


    // Send a title message to all players indicating the new day
    GlobalNotifier.sendTitle("#D19A53&k||&r #8660B4" + ChatUtils.font(dayDisplay + " Día") + " #D19A53&k||&r", ChatUtils.font("#B68BD5" + dayValue + "&8/#9D53D1" + dayList.size()));
    // Broadcast a message to all players welcoming them to the new day
    GlobalNotifier.broadcast("#979CCCBienvenidos viajeros, a su #6E78D1" + dayDisplay.toLowerCase() + " día #979CCCen el reino de Palea.");
    // Play sounds to enhance the atmosphere of the new day
    GlobalNotifier.playSound(Sound.ENTITY_PLAYER_LEVELUP, 0.7f);
    GlobalNotifier.playSound(Sound.BLOCK_BELL_RESONATE, 0.75f);
    GlobalNotifier.playSound("epiphany.ambient.clocktower", 1.15f);
  }



  @EventHandler
  private void hourlyTick(HourlyTickEvent event) {
    // Get the "world" from Bukkit
    var world = Bukkit.getWorld("world");
    // If the world is null, return
    if (world == null) return;

    // Get the new hour from the event
    var newHour = event.getNewHour();
    // If the new hour is 0, set it to 24
    if (newHour == 0) newHour = 24;

    // Calculate the time difference to elapse to the specified hour
    var timeQuery = world.getTime();
    var timeToElapse = (newHour * 1000L) - timeQuery;

    // Play a sound when the hourly tick occurs
    GlobalNotifier.playSound(Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1.25f);


    // Calculate minutes increment based on the time to elapse
    var absTimeToElapse = Math.abs(timeToElapse);
    var minutesIncrement = 60.0 / absTimeToElapse;

    // Iterate through each tick to gradually elapse the time
    for (long i = 0; i < absTimeToElapse; i++) {
      final var delay = i * 50L / absTimeToElapse;
      final var finalI = i;
      final var finalHour = newHour;


      // Schedule a task to run after a certain delay
      Bukkit.getScheduler().runTaskLater(Epiphany.getInstance(), () -> {
        // Calculate the tick based on elapsed time and specified final hour
        var tick = ((double) finalI / timeToElapse) + (finalHour - 1);
        // Adjust tick if the final hour is before the previous hour
        if (finalHour - event.getPreviousHour() <= 0) tick = ((double) finalI / timeToElapse) + (finalHour + 1);

        // Set the time in the world based on the elapsed time
        if (timeToElapse >= 0) world.setTime(timeQuery + finalI);
        else world.setTime(timeQuery - finalI);

        // Calculate minutes based on elapsed time
        var minutes = (int) (finalI * minutesIncrement);


        // Update TimeBar information
        TimeBar.hour = finalHour != 24 ? (finalHour - event.getPreviousHour() > 0 ? finalHour - 1 : finalHour) : TimeManager.getHour();
        TimeBar.minutes = finalHour != 24 ? (finalHour - event.getPreviousHour() > 0 ? minutes : 59 - minutes) : 0;

        // Reset minutes and hour if it exceeds 60
        if (minutes >= 60) {
          TimeBar.minutes = 0;
          TimeBar.hour = TimeManager.getHour();
        }

        // Perform additional actions when the last tick is reached
        if (finalI == absTimeToElapse - 1) {
          // Play a sound when the time is fully elapsed
          GlobalNotifier.playSound(Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 0.85f);

          // Reset TimeBar if it reaches 59 minutes
          if (TimeBar.minutes == 59) {
            TimeBar.hour = TimeManager.getHour();
            TimeBar.minutes = 0;
            TimeBar.tick(tick + 0.001);
          }

          // Reset TimeBar and set the day when the final hour is 24
          if (finalHour == 24) {
            TimeBar.day = TimeManager.getDay();
            TimeBar.minutes = 0;
            TimeBar.tick(0);
          }

        } else {
          // Continue ticking for each iteration
          TimeBar.tick(tick + 0.001);
        }
      }, delay);
    }

  }



}
