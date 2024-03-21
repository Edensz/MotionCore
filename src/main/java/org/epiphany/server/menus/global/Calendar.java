package org.epiphany.server.menus.global;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.epiphany.api.MenuApi;
import org.epiphany.epiphany.items.MenuItems;
import org.epiphany.epiphany.mechanics.time.DayCreator;


public class Calendar extends MenuApi {
  public Calendar(Player player) {
    super(null, 54, "", player, player.getName());
  }


  @Override
  public void initInventory() {
    setOutline(MenuItems.PURPLE.build());

    for (DayCreator day : DayCreator.values()) {
      inventory.addItem(day.getIcon());
    }

  }


  @EventHandler
  public void click(InventoryClickEvent event) {
    if (!event.getInventory().equals(inventory)) return;

    event.setCancelled(true);

  }

}
