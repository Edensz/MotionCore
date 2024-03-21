package org.epiphany.server.roles;

import lombok.Getter;

@Getter
public enum RoleCreator {

  ADMIN('\uE001', "#9D7AD6", "Administrador", "#BEAED8", 1),
  MOD('\uE002', "#7A80D6", "Moderador", "#AFB2D9", 2),
  VIP('\uE003', "#D68987", "VIP", "#DAB4B3", 3),
  MEMBER('\uE004', "#73CA9D", "Miembro", "#B1CBBE", 4),

  ;

  private final Character logo;
  private final String teamColor;
  private final String chatColor;
  private final String display;
  private final Integer priority;

  RoleCreator(Character logo, String teamColor, String display, String chatColor, Integer priority) {
    this.teamColor = teamColor;
    this.logo = logo;
    this.priority = priority;
    this.display = display;
    this.chatColor = chatColor;
  }

}
