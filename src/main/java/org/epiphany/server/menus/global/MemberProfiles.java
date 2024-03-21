package org.epiphany.server.menus.global;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.epiphany.api.MenuApi;
import org.epiphany.api.ItemBuilder;
import org.epiphany.epiphany.items.MenuItems;
import org.epiphany.epiphany.player.MemberList;


public class MemberProfiles extends MenuApi {
  public MemberProfiles(Player player) {
    super(null, 54, "", player, null);
  }



  @Override
  public void initInventory() {
    setOutline(MenuItems.BLANK.build());

    for (MemberList playerList : MemberList.toList()) inventory.addItem(MenuItems.getPlayerProfile(playerList.getName));
  }



  @EventHandler
  public void click(InventoryClickEvent event) {
    if (!event.getInventory().equals(inventory)) return;

    var player = (Player) event.getWhoClicked();
    var currentItem = event.getCurrentItem();
    var clickedItem = new ItemBuilder(event.getCurrentItem());

    event.setCancelled(true);

    if (currentItem == null || currentItem.getItemMeta() == null || clickedItem.getID().equals("class") || currentItem.getType().isAir()) return;

    if (clickedItem.getID().equals("player")) {
      var owner = clickedItem.getPlayer();

      new PersonalProfile(player, owner).open();
    }

  }



}
