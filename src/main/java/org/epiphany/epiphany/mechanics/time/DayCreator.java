package org.epiphany.epiphany.mechanics.time;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.epiphany.api.ItemBuilder;
import org.epiphany.utils.ChatUtils;

import java.util.Arrays;
import java.util.List;


@Getter
public enum DayCreator {

  FIRST(1, "Primer", Relevance.HIGH, new ItemBuilder(Material.BEACON).setLore("")),
  SECOND(2, "Segundo", Relevance.MEDIUM, new ItemBuilder(Material.IRON_SWORD)),
  THIRD(3, "Tercer", Relevance.LOW, new ItemBuilder(Material.IRON_SWORD)),

  ;


  private final int value;
  private final String display;
  private final Relevance relevance;
  private final ItemStack icon;

  DayCreator(int value, String display, Relevance relevance, ItemBuilder icon) {
    this.value = value;
    this.display = display;
    this.relevance = relevance;

    this.icon = icon.setDisplayName("#D29456☀ #CFA071" + ChatUtils.font(display + " Día") + " #D29456☀").build();
  }


  public enum Relevance{HIGH,MEDIUM,LOW}

  public static List<DayCreator> toList() {return Arrays.stream(DayCreator.values()).toList();}

}
