package com.kankangames.shadowofroles.models.roles.enums;

import com.kankangames.shadowofroles.models.player.Player;

public enum AbilityType {

    ACTIVE_OTHERS{
        @Override
        public boolean canUseAbility(Player roleOwner, Player target){
            return !roleOwner.isSamePlayer(target);
        }

    },
    ACTIVE_SELF{
        @Override
        public boolean canUseAbility(Player roleOwner, Player target){
            return roleOwner.isSamePlayer(target);
        }

    },
    ACTIVE_ALL{
        @Override
        public boolean canUseAbility(Player roleOwner, Player target){
            return true;
        }

    },
    OTHER_THAN_CORRUPTER{
        @Override
        public boolean canUseAbility(Player roleOwner, Player target){
            return target.getRole().getTemplate().getWinningTeam().getTeam() != Team.CORRUPTER;
        }

    },
    PASSIVE{
        @Override
        public boolean canUseAbility(Player roleOwner, Player target) {
            return false;
        }
    },
    NO_ABILITY{
        @Override
        public boolean canUseAbility(Player roleOwner, Player target) {
            return false;
        }
    };

    public abstract boolean canUseAbility(Player roleOwner, Player target);


}
