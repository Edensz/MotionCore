package org.epiphany.server.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;


@Getter
public class DayElapseEvent extends Event {
  private final int newDay;
  private final int previousDay;

  public DayElapseEvent(int newDay, int previousDay) {
    this.newDay = newDay;
    this.previousDay = previousDay;
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
