package org.epiphany.server.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;


@Getter
public class HourlyTickEvent extends Event {
  private final int newHour;
  private final int previousHour;

  public HourlyTickEvent(int newHour, int previousHour) {
    this.newHour = newHour;
    this.previousHour = previousHour;
  }


  private static final HandlerList handlers = new HandlerList();

  @NotNull
  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  @NotNull
  public static HandlerList getHandlerList() {return handlers;}

}
