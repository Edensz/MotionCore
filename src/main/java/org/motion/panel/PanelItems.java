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
    PREVIOUS_PAGE(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZkYWI3MjcxZjRmZjA0ZDU0NDAyMTkwNjdhMTA5YjVjMGMxZDFlMDFlYzYwMmMwMDIwNDc2ZjdlYjYxMjE4MCJ9fX0=")).setDisplayName("#A0A0A0Página Anterior").setID("previous")),
    NEXT_PAGE(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFhMTg3ZmVkZTg4ZGUwMDJjYmQ5MzA1NzVlYjdiYTQ4ZDNiMWEwNmQ5NjFiZGM1MzU4MDA3NTBhZjc2NDkyNiJ9fX0=")).setDisplayName("#A0A0A0Página Siguiente").setID("next")),
    ;

    public final ItemStack build;
    PanelItems(@NotNull ItemBuilder itemBuilder) {this.build = itemBuilder.build();}


    @Nullable
    public static ItemStack getCinematicIcon(String cinematicName) {
        final var cinematic = PluginFileAPI.getFolder(cinematicName, CinematicHelper.cinematicsFolder);
        final var cinematicProperties = PluginFileAPI.getFile(cinematic, "properties");
        final var cinematicConfig = PluginFileAPI.getFileConfig(cinematicProperties);

        final var cinematicPlaying = (cinematicConfig.getInt("properties.playersWatching") != 0);
        final var cinematicType = cinematicConfig.getString("properties.type");
        if (cinematicType == null) return null;

        final var cinematicInfoBaseColor = getCinematicInfoColor(cinematicType, true);
        final var cinematicInfoLightColor = getCinematicInfoColor(cinematicType, false);

        if (!cinematicType.equals("movie")) return new ItemBuilder(getCinematicIconMaterial(cinematicType)).setDisplayName("&7• " + cinematicInfoBaseColor + cinematicName + " &7•").setLore(
                "",
                "&7•&r " + cinematicInfoBaseColor + "Nombre del Autor&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.author"),
                "&7•&r " + cinematicInfoBaseColor + "Duración Total&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.totalDuration"),
                "&7•&r " + cinematicInfoBaseColor + "Cuadros por Segundo&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.frameRate"),
                "&7•&r " + cinematicInfoBaseColor + "Fotogramas grabados&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.framesRecorded"),
                "",
                "&7•&r " + cinematicInfoBaseColor + "Vistas Totales&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.timesPlayed"),
                "&7•&r " + cinematicInfoBaseColor + "Vistas Actuales&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.playersWatching"),
                ""
        ).setGlint(cinematicPlaying).setID(cinematicName).build();

        else return new ItemBuilder(getCinematicIconMaterial(cinematicType)).setDisplayName("&7• " + cinematicInfoBaseColor + cinematicName + " &7•").setLore(
                "",
                "&7•&r " + cinematicInfoBaseColor + "Nombre del Autor&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.author"),
                "&7•&r " + cinematicInfoBaseColor + "Duración Total&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.totalDuration"),
                "&7•&r " + cinematicInfoBaseColor + "Cuadros por Segundo&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.frameRate"),
                "&7•&r " + cinematicInfoBaseColor + "Fotogramas grabados&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.framesRecorded"),
                "",
                "&7•&r " + cinematicInfoBaseColor + "Vistas Totales&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.timesPlayed"),
                "&7•&r " + cinematicInfoBaseColor + "Vistas Actuales&7: " + cinematicInfoLightColor + cinematicConfig.getString("properties.playersWatching"),
                "",
                "&7•&r " + cinematicInfoBaseColor + "Marcos Negros&7: " + cinematicInfoLightColor + CinematicHelper.getSpanishModeLabel(Objects.requireNonNull(cinematicConfig.getString("movie.letterBoxBars"))),
                "&7•&r " + cinematicInfoBaseColor + "Interpolación&7: " + cinematicInfoLightColor + CinematicHelper.getSpanishModeLabel(Objects.requireNonNull(cinematicConfig.getString("movie.interpolation"))),
                "&7•&r " + cinematicInfoBaseColor + "Música&7: " + cinematicInfoLightColor + CinematicHelper.getSpanishModeLabel(Objects.requireNonNull(cinematicConfig.getString("movie.musicToPlay"))),
                ""
        ).setGlint(cinematicPlaying).setID(cinematicName).build();
    }

    private static Material getCinematicIconMaterial(@NotNull String cinematicType) {
        var cinematicIconMaterial = Material.BARRIER;
        switch (cinematicType) {
            case "chain" -> cinematicIconMaterial = Material.WARPED_HANGING_SIGN;
            case "basic" -> cinematicIconMaterial = Material.OAK_HANGING_SIGN;
            case "movie" -> cinematicIconMaterial = Material.CRIMSON_HANGING_SIGN;
        }
        return cinematicIconMaterial;
    }
    private static String getCinematicInfoColor(@NotNull String cinematicType, boolean baseColor) {
        var cinematicIconBaseColor = "&5";
        var cinematicIconLightColor = "&5";
        switch (cinematicType) {
            case "chain" -> {
                cinematicIconBaseColor = "#6F8CBB";
                cinematicIconLightColor = "#94A5C1";
            }
            case "basic" -> {
                cinematicIconBaseColor = "#D2915F";
                cinematicIconLightColor = "#D5A885";
            }
            case "movie" -> {
                cinematicIconBaseColor = "#BF5D5D";
                cinematicIconLightColor = "#BA6C6C";
            }
        }
        if (baseColor) return cinematicIconBaseColor;
        else return cinematicIconLightColor;
    }
}

