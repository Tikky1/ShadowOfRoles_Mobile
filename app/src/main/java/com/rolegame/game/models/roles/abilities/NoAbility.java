package com.rolegame.game.models.roles.abilities;

import com.rolegame.game.models.roles.enums.AbilityResult;

public interface NoAbility {
    default AbilityResult doNothing(){
        return AbilityResult.NO_ABILITY_EXIST;
    }
}
