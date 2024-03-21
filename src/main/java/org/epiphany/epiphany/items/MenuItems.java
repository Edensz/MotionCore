package org.epiphany.epiphany.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.epiphany.api.FileApi;
import org.epiphany.api.ItemBuilder;
import org.epiphany.api.SkullCreator;
import org.epiphany.utils.ChatUtils;
import org.epiphany.utils.GlobalUtils;

public enum MenuItems {


  CLOSE(new ItemBuilder(Material.BARRIER).setDisplayName("&cCerrar").setID("close")),
  BLANK(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank").addNBT("blank", 1)),

  RED(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  BLUE(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  PURPLE(new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  ORANGE(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  PINK(new ItemBuilder(Material.PINK_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  MAGENTA(new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  YELLOW(new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  CYAN(new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  GRAY(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  BLACK(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  WHITE(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),
  GREEN(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(" ").setID("blank")),

  COLOR(new ItemBuilder(Material.BRUSH).setDisplayName(ChatUtils.font("#E09D7ECambiar Color")).setID("color")),
  GLINT(new ItemBuilder(Material.NETHER_STAR).setDisplayName(ChatUtils.font("#B87EE0Alternar Brillo")).setID("glint")),
  MEMBERS(new ItemBuilder(Material.RECOVERY_COMPASS).setDisplayName(ChatUtils.font("#81A2D6Lista de Miembros")).setID("members")),

  OVERWORLD(new ItemBuilder(Material.GRASS_BLOCK).setDisplayName("&f" + ChatUtils.font("Overworld")).setID("Overworld")),

  NEXT(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFhMTg3ZmVkZTg4ZGUwMDJjYmQ5MzA1NzVlYjdiYTQ4ZDNiMWEwNmQ5NjFiZGM1MzU4MDA3NTBhZjc2NDkyNiJ9fX0=")).setDisplayName("#A0A0A0Página Siguiente").setID("next").setGlint()),
  PREVIOUS(new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZkYWI3MjcxZjRmZjA0ZDU0NDAyMTkwNjdhMTA5YjVjMGMxZDFlMDFlYzYwMmMwMDIwNDc2ZjdlYjYxMjE4MCJ9fX0=")).setDisplayName("#A0A0A0Página Anterior").setID("previous").setGlint()),


  ;

  private final ItemBuilder item;

  MenuItems(ItemBuilder builder) {
    this.item = builder;
  }

  public ItemStack build() {
    return item.build();
  }

  public ItemBuilder getBuilder() {return item;}


  public static ItemStack getPlayerProfile(String name) {
    var config = FileApi.getFileConfig(FileApi.getFile(FileApi.getFolder("PlayerData"), name));
    var display = config.getString("display");

    return new ItemBuilder(Material.PLAYER_HEAD)
      .setDisplayName(ChatUtils.font("#CE9B6DPerfil de " + display)).setOwner(display).setLore(
        "",
        "#CE9B6D⌚ #CDA785Tiempo Jugado&8: #D0B7A0" + GlobalUtils.formatSeconds(config.getInt("timePlayed")),
        "#CE9B6D☀ #CDA785Contenido Completado&8: #D0B7A0" + GlobalUtils.completionPercentage(config.getInt("completion")),
        "#CE9B6D☄ #CDA785Nivel de Magia&8: #D0B7A0" + config.getInt("magicLevel"),
        "#CE9B6D☠ #CDA785Número de Muertes&8: #D0B7A0" + config.getInt("deaths"),
        ""
      ).setGlint().setID("player").setPlayer(name).build();
  }

}

