package com.kankangames.shadowofroles.models.roles.abilities;

import com.kankangames.shadowofroles.models.player.Player;

import java.util.List;

public interface RoleSpecificValuesChooser {

    void chooseRoleSpecificValues(List<Player> choosablePlayers);
}
