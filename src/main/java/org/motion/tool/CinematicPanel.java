package org.motion.tool;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.motion.utils.ItemBuilder;
import org.motion.utils.MenuAPI;
import org.motion.utils.PlayerHandler;

import java.util.Objects;


public class CinematicPanel extends MenuAPI {
    public CinematicPanel(Player player, PanelMode panelMode) {
        super(null, 54, "#A9AAB9&l•&r #797B8EPanel de Cinemáticas #A9AAB9&l•", player, panelMode);
    }


    @Override
    public void initInventory() {
        final var blankItem = new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank").build();

        super.empty();
        super.setOutline(blankItem);

        switch (panelMode) {
            case VIEW -> {
                var cinematicList = CinematicHelper.cinematicListAsArray();

                super.setOutline(blankItem);

                if (cinematicList == null) return;
                for (String cinematics : cinematicList) {
                    var cinematicIcon = CinematicHelper.setCinematicIcon(cinematics);
                    inventory.addItem(Objects.requireNonNull(cinematicIcon));
                }
            }
            case MOVIE -> {}
            case CHAIN -> {}
        }
    }


    @EventHandler
    public void click(@NotNull InventoryClickEvent event) {
        if (event.getInventory() != inventory) return;
        event.setCancelled(true);

        var item = event.getCurrentItem();
        if (item == null) return;

        var itemID = new ItemBuilder(item).getID();
        if (itemID == null || itemID.equalsIgnoreCase("blank")) return;

        PlayerHandler.playSound(Sound.UI_BUTTON_CLICK,1.35f, player);

        if (CinematicHelper.doesCinematicExist(itemID)) {
            new CinematicManager(player, itemID).play();
            inventory.close();

            PlayerHandler.sendMessage("#CFB39CReproduciendo la cinemática #D78E51" + itemID + "#CFB39C.", player);
            PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP,1.35f, player);
            PlayerHandler.playSound(Sound.BLOCK_PISTON_EXTEND,1.35f, player);
        }
    }

}
