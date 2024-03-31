package org.motion.tool;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.motion.MotionCore;
import org.motion.player.PlayerFileHelper;
import org.motion.utils.ItemBuilder;
import org.motion.utils.MenuAPI;
import org.motion.player.PlayerHandler;
import org.motion.utils.PluginFileAPI;

import java.io.IOException;
import java.util.Objects;


public class CinematicPanel extends MenuAPI {
    public CinematicPanel(Player player, PanelMode panelMode, @Nullable String cinematicName) {
        super(null, 54, "#A9AAB9&l•&r #797B8EPanel de Cinemáticas #A9AAB9&l•", player, panelMode, cinematicName);
    }

    @Override
    public void initInventory() { this.setPanelModeItems(panelMode); }


    @EventHandler
    public void click(@NotNull InventoryClickEvent event) {
        if (event.getInventory() != inventory) return;
        event.setCancelled(true);

        final var item = event.getCurrentItem();
        if (item == null) return;
        final var itemID = new ItemBuilder(item).getID();
        if (itemID == null || itemID.equalsIgnoreCase("blank")) return;

        if (panelMode.equals(PanelMode.MOVIE)) {
            final var cinematicFolder = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
            final var cinematicProperties = PluginFileAPI.getFile(cinematicFolder, "properties");
            final var cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);

            if (itemID.equalsIgnoreCase(cinematicName)) {
                PlayerHandler.sendMessage("#C4857FSe grabará cada movimiento que hagas una vez que el contador termine.", player);
                PlayerHandler.playSound(Sound.BLOCK_BELL_USE, 1.1f, player);
                inventory.close();

                for (int countdown = 0; countdown < 5; countdown++) {
                    var pitch = (float) (1 - (0.05 * countdown));
                    var delay = countdown * 20;
                    PlayerHandler.sendDelayedActionBar("&8• &7Empezando en &f" + (5 - countdown) + "&r &8•", player, delay);
                    PlayerHandler.playDelayedSound(Sound.BLOCK_AMETHYST_BLOCK_PLACE, pitch , player, delay);
                    PlayerHandler.playDelayedSound(Sound.UI_BUTTON_CLICK, pitch, player, delay);
                }

                Bukkit.getScheduler().runTaskLater(MotionCore.getInstance(), () -> {
                    PlayerFileHelper.updatePlayerFile(player, PlayerFileHelper.Status.RECORDING, false);
                    new CinematicManager(player, cinematicName).record(cinematicConfig.getInt("properties.frameRate"), true);
                    PlayerHandler.playSound(Sound.BLOCK_CHEST_OPEN,1.25F, player);
                    PlayerHandler.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.25f, player);
                    PlayerHandler.playSound(Sound.UI_BUTTON_CLICK, 1.25f, player);
                }, 100);

                return;
            }

            final var optionPath = "movie." + itemID;
            final var actualValue = cinematicConfig.getBoolean(optionPath);
            cinematicConfig.set(optionPath, !actualValue);

            if (itemID.equalsIgnoreCase("playMusic")) {
                if (actualValue) cinematicConfig.set("movie.musicToPlay", "false");
                else {
                    PlayerFileHelper.setCreatingMode(player, true, cinematicName);
                    PlayerHandler.playSound(Sound.BLOCK_CHEST_CLOSE, 0.95f, player);
                    PlayerHandler.playSound(Sound.BLOCK_ENDER_CHEST_CLOSE, 0.85f, player);
                    inventory.close();
                    return;
                }
            }

            try {cinematicConfig.save(cinematicProperties);}
            catch (IOException ignored) {}
            this.setPanelModeItems(panelMode);
        }

        if (CinematicHelper.doesCinematicExist(itemID)) {
            new CinematicManager(player, itemID).play(player);
            inventory.close();
        }

        PlayerHandler.playSound(Sound.UI_BUTTON_CLICK,1.35f, player);
    }


    private void setPanelModeItems(@NotNull PanelMode panelMode) {
        super.empty();
        switch (panelMode) {
            case VIEW -> {
                final var cinematicList = CinematicHelper.cinematicListAsArray();
                final var cyanGlassPane = new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank").build();

                super.setOutline(cyanGlassPane);

                if (cinematicList == null) return;
                for (String cinematics : cinematicList) {
                    var cinematicIcon = CinematicHelper.getCinematicIcon(cinematics);
                    inventory.addItem(Objects.requireNonNull(cinematicIcon));
                }
            }

            case MOVIE -> {
                var cinematicFolder = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
                var cinematicProperties = PluginFileAPI.getFile(cinematicFolder, "properties");
                var cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);

                var redGlassPane = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank").build();
                var letterBoxBars = new ItemBuilder(Material.BLACK_CONCRETE).setDisplayName("#9B9B9B• #808080Lᴇᴛᴛᴇʀʙᴏxɪɴɢ #9B9B9B•").setLore("#9B9B9BEsta opción agregará barras", "#9B9B9Bnegras a tu cinemática.", "", "#D26767¡Requiere de un Resource Pack!", "").setGlint(cinematicConfig.getBoolean("movie.letterBoxBars")).setID("letterBoxBars");
                var playMusic = new ItemBuilder(Material.JUKEBOX).setDisplayName("#B19287• #B0745FPʟᴀʏ Mᴜsɪᴄ #B19287•").setLore("#B19287Esta opción reproducirá un", "#B19287sonido de tu elección mientras", "#B19287la cinemática este activada.", "", "#D26767¡Se recomienda un Resource Pack!", "").setGlint(cinematicConfig.getBoolean("movie.playMusic")).setID("playMusic");
                var interpolation = new ItemBuilder(Material.SPYGLASS).setDisplayName("#C3A077• #C08F54Iɴᴛᴇʀᴘᴏʟᴀᴛɪᴏɴ #C3A077•").setLore("#C3A077Esta opción reproducirá una", "#C3A077transición al iniciar y", "#C3A077terminar la cinemática.", "", "#D26767¡Requiere de un Resource Pack!", "").setGlint(cinematicConfig.getBoolean("movie.interpolation")).setID("interpolation");

                super.setOutline(redGlassPane);

                inventory.setItem(13, CinematicHelper.getCinematicIcon(cinematicName));
                inventory.setItem(29, letterBoxBars.build());
                inventory.setItem(31, playMusic.build());
                inventory.setItem(33, interpolation.build());
            }

            case CHAIN -> {}
        }
    }

}
