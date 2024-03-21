package org.epiphany.server.menus.staff;

import lombok.Getter;
import org.epiphany.api.ItemBuilder;
import org.epiphany.api.MenuApi;
import org.epiphany.epiphany.items.CustomItems;
import org.epiphany.epiphany.items.MenuItems;
import org.epiphany.utils.ChatUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class ItemList extends MenuApi {
  private int page = 1;
  public ItemList(Player player) {super(null, 54, "#CC813F☀ #CE8C52&lServer Items", player, null);}


  @Override
  public void initInventory() {
    this.page = 1;

    updateUnsortedMenu();

    setItem(45, MenuItems.PREVIOUS.build());
    setItem(53, MenuItems.NEXT.build());
  }


  @EventHandler
  public void click(InventoryClickEvent event) {
    if (!event.getInventory().equals(inventory)) return;

    var player = (Player) event.getWhoClicked();
    var currentItem = event.getCurrentItem();
    var clickedItem = new ItemBuilder(event.getCurrentItem());

    event.setCancelled(true);

    if (currentItem == null || currentItem.getItemMeta() == null || clickedItem.getID().equals("class") || currentItem.getType().isAir()) return;

    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,1,2f);


    switch (clickedItem.getID()) {
      // Pass to the next item page
      case "next" -> {
        if (!isFull()) {
          ChatUtils.error("La página siguiente aún no está disponible.", player);
          return;
        }

        this.page = page + 1;
        updateUnsortedMenu();

        player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1, 1.11f);
        return;
      }
      // Goes back to the previous item page
      case "previous" -> {
        // Returns if the page is the first one
        if (this.page == 1) {
          ChatUtils.error("No puedes regresar mas paginas.", player);
          return;
        }

        this.page = page - 1;

        updateUnsortedMenu();

        player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, 1, 1.11f);
        return;
      }
    }

    player.getInventory().addItem(clickedItem.build());

  }


  // Updates the Unsorted Item Menu depending on the current page
  private void updateUnsortedMenu() {
    var customItems = CustomItems.list();
    final var startItem = (51 * page) - 51;

    inventory.clear();

    setItem(45, MenuItems.PREVIOUS.build());
    setItem(53, MenuItems.NEXT.build());

    for (int i = startItem; i < customItems.size(); i++) {
      var slot = i - startItem;

      if (slot >= 45) slot = slot + 1;
      if (slot >= 53) return;

      setItem(slot, customItems.get(i).build());
    }

  }


}
