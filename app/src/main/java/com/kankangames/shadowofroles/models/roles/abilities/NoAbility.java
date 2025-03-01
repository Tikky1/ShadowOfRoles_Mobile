package com.kankangames.shadowofroles.models.roles.abilities;

import com.kankangames.shadowofroles.models.roles.enums.AbilityResult;

public interface NoAbility {
    default AbilityResult doNothing(){
        return AbilityResult.NO_ABILITY_EXIST;
    }
}
