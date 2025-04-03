package com.kankangames.shadowofroles.game.models.roles.otherinterfaces;

import com.kankangames.shadowofroles.game.models.player.Player;

import java.util.List;

public interface RoleSpecificValuesChooser {

    void chooseRoleSpecificValues(List<Player> choosablePlayers);
}
