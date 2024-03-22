package me.cinematic.player;

import me.cinematic.Cinematic;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class PlayerData {


  public static <T, Z> Z get(PersistentDataHolder holder, String key, PersistentDataType<T, Z> type) {
    if (!PlayerData.has(holder, key, type)) {return null;}

    return PlayerData.getDataContainer(holder).get(PlayerData.Key(key), type);
  }


  public static <T, Z> boolean has(PersistentDataHolder holder, String key, PersistentDataType<T, Z> type) {
    if (holder == null || holder.getPersistentDataContainer().get(PlayerData.Key(key), type) == null) return false;

    return PlayerData.getDataContainer(holder).has(PlayerData.Key(key), type);
  }


  public static <T, Z> boolean equals(PersistentDataHolder holder, String key, PersistentDataType<T, Z> type, Z value) {
    if (!PlayerData.has(holder, key, type)) return false;

    return Objects.equals(PlayerData.get(holder, key, type), value) || value.equals(PlayerData.get(holder, key, type));
  }


  public static <T, Z> void set(PersistentDataHolder holder, String key, PersistentDataType<T, Z> type, Z value) {
    PlayerData.getDataContainer(holder).set(PlayerData.Key(key), type, value);
  }


  public static NamespacedKey Key(String string){
    return new NamespacedKey(Cinematic.getInstance(), string);
  }


  private static PersistentDataContainer getDataContainer(PersistentDataHolder holder){
    return holder.getPersistentDataContainer();
  }


}
