package org.motion.panel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.motion.MotionCore;
import org.motion.player.PlayerFileHelper;
import org.motion.tool.CinematicHelper;
import org.motion.tool.CinematicManager;
import org.motion.utils.ItemBuilder;
import org.motion.utils.MenuAPI;
import org.motion.player.PlayerHandler;
import org.motion.utils.PluginFileAPI;

import java.io.IOException;

public class CinematicPanel extends MenuAPI {
    private int currentPanelPage;
    private final int taskID;

    public CinematicPanel(Player player, PanelMode panelMode, @Nullable String cinematicName) {
        super(null, 54, "#A9AAB9&l•&r #797B8EPanel de Cinemáticas #A9AAB9&l•", player, panelMode, cinematicName);
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(MotionCore.getInstance(), this::initInventory, 0, 10);
    }


    @Override
    public void initInventory() {
        super.empty();
        switch (panelMode) {
            case VIEW -> {
                final var pluginConfig = MotionCore.getInstance().getConfig();
                final var cinematicList = CinematicHelper.cinematicListAsArray();
                if (cinematicList == null) return;

                super.setOutline(PanelItems.CYAN_GLASS_PANE.build);
                super.setItem(46, PanelItems.PREVIOUS_PAGE.build);
                super.setItem(52, PanelItems.NEXT_PAGE.build);

                final var initialValueToIndex = 28 * currentPanelPage;
                for (int i = initialValueToIndex; i < cinematicList.size(); i++) {
                    final var cinematic = cinematicList.get(i);
                    final var cinematicType = CinematicHelper.getCinematicType(cinematic);

                    final var cinematicIcon = PanelItems.getCinematicIcon(cinematic);
                    final var showMovies = pluginConfig.getBoolean("cinematicPanel.showMovies");
                    final var showBasics = pluginConfig.getBoolean("cinematicPanel.showBasics");
                    final var showChains = pluginConfig.getBoolean("cinematicPanel.showChains");
                    if (cinematicType == null || cinematicIcon == null
                            || !showMovies && cinematicType.equals(CinematicHelper.CinematicType.MOVIE)
                            || !showBasics && cinematicType.equals(CinematicHelper.CinematicType.BASIC)
                            || !showChains && cinematicType.equals(CinematicHelper.CinematicType.CHAIN)
                    ) continue;

                    inventory.addItem(cinematicIcon);
                }

                var showMoviesIcon = new ItemBuilder(Material.CRIMSON_SIGN).setDisplayName("#BA6C6C• #BF5D5DMᴏsᴛʀᴀʀ Pᴇʟɪᴄᴜʟᴀs #BA6C6C•").setID("showMovies").setGlint(pluginConfig.getBoolean("cinematicPanel.showMovies"));
                var showBasicsIcon = new ItemBuilder(Material.OAK_SIGN).setDisplayName("#D5A885• #D2915FMᴏsᴛʀᴀʀ Bᴀsɪᴄᴀs #D5A885•").setID("showBasics").setGlint(pluginConfig.getBoolean("cinematicPanel.showBasics"));
                var showChainsIcon = new ItemBuilder(Material.WARPED_SIGN).setDisplayName("#94A5C1• #6F8CBBMᴏsᴛʀᴀʀ Cᴀᴅᴇɴᴀs #94A5C1•").setID("showChains").setGlint(pluginConfig.getBoolean("cinematicPanel.showChains"));
                super.setItem(48, showMoviesIcon.build());
                super.setItem(49, showBasicsIcon.build());
                super.setItem(50, showChainsIcon.build());
            }

            case MOVIE -> {
                super.setOutline(PanelItems.RED_GLASS_PANE.build);
                super.setItem(13, PanelItems.getCinematicIcon(cinematicName));

                final var cinematicFolder = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
                final var cinematicProperties = PluginFileAPI.getFile(cinematicFolder, "properties");
                final var cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);

                var letterBoxBars = new ItemBuilder(Material.BLACK_CONCRETE).setDisplayName("#9B9B9B• #808080Lᴇᴛᴛᴇʀʙᴏxɪɴɢ #9B9B9B•").setLore("#9B9B9BEsta opción agregará barras", "#9B9B9Bnegras a tu cinemática.", "", "#D26767¡Requiere de un Resource Pack!", "").setGlint(cinematicConfig.getBoolean("movie.letterBoxBars")).setID("letterBoxBars");
                var playMusic = new ItemBuilder(Material.JUKEBOX).setDisplayName("#B19287• #B0745FPʟᴀʏ Mᴜsɪᴄ #B19287•").setLore("#B19287Esta opción reproducirá un", "#B19287sonido de tu elección mientras", "#B19287la cinemática este activada.", "", "#D26767¡Se recomienda un Resource Pack!", "").setGlint(cinematicConfig.getBoolean("movie.playMusic")).setID("playMusic");
                var interpolation = new ItemBuilder(Material.SPYGLASS).setDisplayName("#C3A077• #C08F54Iɴᴛᴇʀᴘᴏʟᴀᴛɪᴏɴ #C3A077•").setLore("#C3A077Esta opción reproducirá una", "#C3A077transición al iniciar y", "#C3A077terminar la cinemática.", "", "#D26767¡Requiere de un Resource Pack!", "").setGlint(cinematicConfig.getBoolean("movie.interpolation")).setID("interpolation");
                super.setItem(29, letterBoxBars.build());
                super.setItem(31, playMusic.build());
                super.setItem(33, interpolation.build());
            }

            case CHAIN -> {}
        }
    }


    @EventHandler
    public void close(@NotNull InventoryCloseEvent event) {
        if (event.getInventory() != inventory) return;
        Bukkit.getScheduler().cancelTask(taskID);
    }

    @EventHandler
    public void click(@NotNull InventoryClickEvent event) {
        if (event.getInventory() != inventory) return;
        event.setCancelled(true);

        final var item = event.getCurrentItem();
        if (item == null) return;
        final var itemID = new ItemBuilder(item).getID();
        if (itemID == null || itemID.equalsIgnoreCase("blank")) return;

        switch (panelMode) {
            case MOVIE -> {
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
                this.initInventory();
            }

            case VIEW -> {
                final var pluginConfig = MotionCore.getInstance().getConfig();
                final var cinematicList = CinematicHelper.cinematicListAsArray();
                if (cinematicList == null) return;

                switch (itemID) {
                    case "next" -> {
                        if (!super.isFull()) {
                            PlayerHandler.errorMessage("No hay más páginas disponibles.", player);
                            return;
                        }
                        this.currentPanelPage = currentPanelPage+1;
                    }
                    case "previous" -> {
                        if (currentPanelPage == 0) {
                            PlayerHandler.errorMessage("No puedes retroceder estando en la primera página.", player);
                            return;
                        }
                        this.currentPanelPage = currentPanelPage-1;
                    }
                    case "showMovies", "showBasics", "showChains" -> {
                        final var newBooleanValue = pluginConfig.getBoolean("cinematicPanel." + itemID);
                        pluginConfig.set("cinematicPanel." + itemID, !newBooleanValue);
                        PlayerHandler.playSound(Sound.UI_LOOM_SELECT_PATTERN, 1.1f, player);
                        MotionCore.getInstance().saveConfig();
                    }
                }

                PlayerHandler.playSound(Sound.UI_LOOM_TAKE_RESULT, 1.35f, player);
                this.initInventory();
            }
        }

        if (CinematicHelper.doesCinematicExist(itemID)) {
            new CinematicManager(player, itemID).play(player);
            inventory.close();
        }

        PlayerHandler.playSound(Sound.UI_BUTTON_CLICK,1.35f, player);
    }

}
