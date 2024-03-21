package org.epiphany.epiphany.items;

import org.bukkit.inventory.ItemStack;
import org.epiphany.api.ItemBuilder;

import java.util.Arrays;
import java.util.List;

public enum CustomItems {



  ;

  private final ItemBuilder item;

  CustomItems(ItemBuilder builder) {
    this.item = builder;
  }

  public ItemStack build() {
    return this.item.build();
  }

  public static List<CustomItems> list() {
    return Arrays.stream(CustomItems.values()).toList();
  }

}

