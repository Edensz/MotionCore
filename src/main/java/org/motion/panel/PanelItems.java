package org.motion.panel;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.motion.tool.CinematicHelper;
import org.motion.utils.ItemBuilder;
import org.motion.utils.PluginFileAPI;
import org.motion.utils.SkullCreator;

import java.util.Objects;

public enum PanelItems {
    RED_GLASS_PANE(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
    CYAN_GLASS_PANE(new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
    GRAY_GLASS_PANE(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
    PREVIOUS_PAGE(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZkYWI3MjcxZjRmZjA0ZDU0NDAyMTkwNjdhMTA5YjVjMGMxZDFlMDFlYzYwMmMwMDIwNDc2ZjdlYjYxMjE4MCJ9fX0=")).setDisplayName("#A0A0A0Página Anterior").setID("previous")),
    NEXT_PAGE(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFhMTg3ZmVkZTg4ZGUwMDJjYmQ5MzA1NzVlYjdiYTQ4ZDNiMWEwNmQ5NjFiZGM1MzU4MDA3NTBhZjc2NDkyNiJ9fX0=")).setDisplayName("#A0A0A0Página Siguiente").setID("next")),
    ;

    public final ItemStack build;
    PanelItems(@NotNull ItemBuilder itemBuilder) {this.build = itemBuilder.build();}


    public static ItemStack getChainCinematicIcon(String chainCinematicName) { return getChainCinematicIcon(chainCinematicName, false); }

    public static ItemStack getChainCinematicIcon(String chainCinematicName, boolean setForcedGlint) {
        final var chainFolder = PluginFileAPI.getFolder(chainCinematicName, CinematicHelper.cinematicsFolder);
        final var chainProperties = PluginFileAPI.getFile(chainFolder, "properties");
        final var chainConfig = PluginFileAPI.getFileConfig(chainProperties);

        var cinematicGlint = chainConfig.getInt("properties.playersWatching") != 0;
        if (setForcedGlint) cinematicGlint = true;

        var chainIcon = new ItemBuilder(Material.WARPED_HANGING_SIGN).setDisplayName("&7• #6F8CBB" + chainCinematicName + " &7•").setLore(
                "",
                "&7•&r #6F8CBBNombre del Autor&7: #94A5C1" + chainConfig.getString("properties.author"),
                "&7•&r #6F8CBBDuración Total&7: #94A5C1" + chainConfig.getString("properties.totalDuration") + " segundos.",
                "&7•&r #6F8CBBFotogramas Totales&7: #94A5C1" + chainConfig.getString("properties.framesRecorded"),
                "",
                "&7•&r #6F8CBBVistas Totales&7: #94A5C1" + chainConfig.getString("properties.timesPlayed"),
                "&7•&r #6F8CBBVistas Actuales&7: #94A5C1" + chainConfig.getString("properties.playersWatching"),
                "",
                "&7•&r #6F8CBBCinemáticas&7:"
        ).setGlint(cinematicGlint).setID(chainCinematicName);

        final var cinematicsList = chainConfig.getStringList("chain.cinematics");
        for (int i = 0; i < cinematicsList.size(); i++) {
            final var cinematicPositionInChain = i + 1;
            final var cinematicName = cinematicsList.get(i);
            final var cinematicProperties = CinematicHelper.getCinematicProperties(cinematicName);
            final var cinematicDuration = cinematicProperties.getInt("properties.totalDuration");
            chainIcon.addLore("&7&l|&r #94A5C1" + cinematicPositionInChain + ". &7[#94A5C1" + cinematicDuration + "s&7] #6F8CBB" + cinematicName);
            if (i == (cinematicsList.size() - 1)) chainIcon.addLore(" ");
        }

        return chainIcon.build();
    }


    public static ItemStack getCinematicIcon(String cinematicName) { return getCinematicIcon(cinematicName, false); }
    @Nullable
    public static ItemStack getCinematicIcon(String cinematicName, boolean setForcedGlint) {
        final var cinematic = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
        final var cinematicProperties = PluginFileAPI.getFile(cinematic, "properties");
        final var cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);

        final var cinematicType = cinematicConfig.getString("properties.type");
        if (cinematicType == null) return null;

        if (cinematicType.equals("chain")) return getChainCinematicIcon(cinematicName);

        var cinematicGlint = cinematicConfig.getInt("properties.playersWatching") != 0;
        if (setForcedGlint) cinematicGlint = true;

        return switch (cinematicType) {
            case "basic" -> new ItemBuilder(Material.OAK_HANGING_SIGN).setDisplayName("&7• #D2915F" + cinematicName + " &7•").setLore(
                    "",
                    "&7•&r #D2915FNombre del Autor&7: #D5A885" + cinematicConfig.getString("properties.author"),
                    "&7•&r #D2915FDuración Total&7: #D5A885" + cinematicConfig.getString("properties.totalDuration") + " segundos.",
                    "&7•&r #D2915FCuadros por Segundo&7: #D5A885" + cinematicConfig.getString("properties.frameRate"),
                    "&7•&r #D2915FFotogramas grabados&7: #D5A885" + cinematicConfig.getString("properties.framesRecorded"),
                    "",
                    "&7•&r #D2915FVistas Totales&7: #D5A885" + cinematicConfig.getString("properties.timesPlayed"),
                    "&7•&r #D2915FVistas Actuales&7: #D5A885"+ cinematicConfig.getString("properties.playersWatching"),
                    ""
            ).setGlint(cinematicGlint).setID(cinematicName).build();

            case "movie" -> new ItemBuilder(Material.CRIMSON_HANGING_SIGN).setDisplayName("&7• #BF5D5D" + cinematicName + " &7•").setLore(
                    "",
                    "&7•&r #BF5D5DNombre del Autor&7: #BA6C6C" + cinematicConfig.getString("properties.author"),
                    "&7•&r #BF5D5DDuración Total&7: #BA6C6C" + cinematicConfig.getString("properties.totalDuration") + " segundos.",
                    "&7•&r #BF5D5DCuadros por Segundo&7: #BA6C6C" + cinematicConfig.getString("properties.frameRate"),
                    "&7•&r #BF5D5DFotogramas grabados&7: #BA6C6C" + cinematicConfig.getString("properties.framesRecorded"),
                    "",
                    "&7•&r #BF5D5DVistas Totales&7: #BA6C6C" + cinematicConfig.getString("properties.timesPlayed"),
                    "&7•&r #BF5D5DVistas Actuales&7: #BA6C6C" + cinematicConfig.getString("properties.playersWatching"),
                    "",
                    "&7•&r #BF5D5DMarcos Negros&7: #BA6C6C" + CinematicHelper.getSpanishModeLabel(Objects.requireNonNull(cinematicConfig.getString("movie.letterBoxBars"))),
                    "&7•&r #BF5D5DInterpolación&7: #BA6C6C" + CinematicHelper.getSpanishModeLabel(Objects.requireNonNull(cinematicConfig.getString("movie.interpolation"))),
                    "&7•&r #BF5D5DMúsica&7: #BA6C6C" +  CinematicHelper.getSpanishModeLabel(Objects.requireNonNull(cinematicConfig.getString("movie.musicToPlay"))),
                    ""
            ).setGlint(cinematicGlint).setID(cinematicName).build();

            default -> null;
        };
    }

}

