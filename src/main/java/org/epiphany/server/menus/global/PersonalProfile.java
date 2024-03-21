package org.epiphany.server.menus.global;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;
import org.epiphany.api.Data;
import org.epiphany.api.FileApi;
import org.epiphany.api.MenuApi;
import org.epiphany.api.ItemBuilder;
import org.epiphany.epiphany.items.MenuItems;
import org.epiphany.server.FileManager;

import java.util.Arrays;


public class PersonalProfile extends MenuApi {
  public PersonalProfile(Player player, String owner) {
    super(null, 54, "", player, owner);
  }
  public enum ProfileColor{RED, BLUE, PURPLE, ORANGE, PINK, MAGENTA, YELLOW, CYAN, BLACK, GRAY, GREEN, WHITE}
  private int colorID = 1;



  @Override
  public void initInventory() {
    empty();
    updateColor(owner);
    setItem(49, MenuItems.MEMBERS.build());
    inventory.setItem(4, MenuItems.getPlayerProfile(owner));

    if (!(player.getName().equalsIgnoreCase(owner))) return;

    setItem(48, MenuItems.COLOR.build());
    setItem(50, MenuItems.GLINT.build());
  }



  @EventHandler
  public void click(InventoryClickEvent event) {
    if (!event.getInventory().equals(inventory)) return;

    var player = (Player) event.getWhoClicked();
    var currentItem = event.getCurrentItem();
    var clickedItem = new ItemBuilder(event.getCurrentItem());

    event.setCancelled(true);

    if (currentItem == null || currentItem.getItemMeta() == null || clickedItem.getID().equals("class") || currentItem.getType().isAir()) return;


    switch (clickedItem.getID()) {
      case "color" -> player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN,1,2);

      case "glint" -> {
        var glint = Data.get(player, "profileGlint", PersistentDataType.BOOLEAN);

        if (glint == null || glint) {
          Data.set(player, "profileGlint", PersistentDataType.BOOLEAN, false);
          FileManager.updatePlayerFile(player.getName());
          player.playSound(player.getLocation(), Sound.BLOCK_GRINDSTONE_USE, 1, 1.25f);
        }
        else {
          Data.set(player, "profileGlint", PersistentDataType.BOOLEAN, true);
          FileManager.updatePlayerFile(player.getName());
          player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1.25f);
        }

        initInventory();
        return;
      }

      case "close" -> {
        initInventory();
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE,1,2);
        return;
      }

      case "next" -> {
        if (colorID >= 11) colorID = -1;

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1.5f);
        colorID = colorID + 1;

        setColor(colorID);
        initInventory();
      }

      case "previous" -> {
        if (colorID == 0) colorID = 12;

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1.5f);
        colorID = colorID - 1;

        setColor(colorID);
        initInventory();
      }

      case "members" -> {
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE,1, 0.85f);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1, 0.85f);

        new MemberProfiles(player).open();
        return;
      }

    }

    setItem(48, MenuItems.PREVIOUS.build());
    setItem(49, MenuItems.CLOSE.build());
    setItem(50, MenuItems.NEXT.build());
  }



  private void updateColor(String owner) {
    var file = FileApi.getFile(FileApi.getFolder("PlayerData"), owner);
    var config = FileApi.getFileConfig(file);

    var color = config.getString("profileColor");
    var glint = config.getBoolean("profileGlint");
    var item = MenuItems.valueOf(color);


    if (glint) setOutline(item.getBuilder().setGlint().build());
    else setOutline(item.getBuilder().removeGlint().build());
  }

  private void setColor(int colorID) {
    var colorList = Arrays.stream(ProfileColor.values()).toList();
    var color = colorList.get(colorID).name();

    Data.set(player, "profileColor", PersistentDataType.STRING, color);

    FileManager.updatePlayerFile(player.getName());
  }


}
