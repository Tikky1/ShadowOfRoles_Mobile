package com.kankangames.shadowofroles.ui.helper;

import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.good.Lorekeeper;

public class PlayerActionVisibility {

    private final boolean isLocalPlayer;
    private final boolean isPlayerAlive;
    private final Player localPlayer, targetPlayer;
    private final Time time;


    public PlayerActionVisibility(Player localPlayer, Player targetPlayer, Time time){
        this.localPlayer = localPlayer;
        this.targetPlayer = targetPlayer;
        this.time = time;

        isLocalPlayer = localPlayer.isSamePlayer(targetPlayer);
        isPlayerAlive = localPlayer.getDeathProperties().isAlive();
    }


    public boolean shouldShowButton(){

        switch (time){

            case DAY:
                return shouldShowButtonDuringDay();

            case VOTING:
                return shouldShowButtonDuringVoting();

            case NIGHT:
                return shouldShowButtonDuringNight();

            default:
                return false;

        }
    }


    private boolean shouldShowButtonDuringDay(){
        return false;
    }

    private boolean shouldShowButtonDuringVoting(){
        return isPlayerAlive && !isLocalPlayer;
    }

    private boolean shouldShowButtonDuringNight(){
        boolean abilityVis = canUseAbilityOnTarget();

        return applySpecialRoleRules(abilityVis);
    }

    private boolean canUseAbilityOnTarget(){

        if(!isPlayerAlive){
            return false;
        }

        return localPlayer.getRole().getTemplate().getAbilityType().canUseAbility(localPlayer, targetPlayer);

    }

    private boolean applySpecialRoleRules(boolean previous){
        boolean visible = previous;
        if (localPlayer.getRole().getTemplate().getId() == RoleID.LORE_KEEPER) {
            Lorekeeper lorekeeper = (Lorekeeper) localPlayer.getRole().getTemplate();
            visible = !lorekeeper.getAlreadyChosenPlayers().contains(targetPlayer);

        }

        else if (localPlayer.getRole().getTemplate().getId() == RoleID.LAST_JOKE) {
            RoleTemplate lastJoke = localPlayer.getRole().getTemplate();
            if(localPlayer.getDeathProperties().isAlive()) visible = false;
            else if(lastJoke.getRoleProperties().abilityUsesLeft() > 0 )
                visible = lastJoke.getAbilityType().canUseAbility(localPlayer, targetPlayer);
        }

        else if(localPlayer.getRole().getTemplate().getId() == RoleID.FOLK_HERO){
            RoleTemplate folkHero = localPlayer.getRole().getTemplate();
            visible = folkHero.getRoleProperties().abilityUsesLeft() > 0;

        }

        return visible;
    }




}
