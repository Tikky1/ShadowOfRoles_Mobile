package com.kankangames.shadowofroles.ui.helper;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support.LastJoke;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.protector.FolkHero;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.good.Lorekeeper;

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
        if (localPlayer.getRole().getTemplate() instanceof Lorekeeper) {
            Lorekeeper lorekeeper = (Lorekeeper) localPlayer.getRole().getTemplate();
            if (lorekeeper.getAlreadyChosenPlayers().contains(targetPlayer)) {
                visible = false;
            }
        }

        else if (localPlayer.getRole().getTemplate() instanceof LastJoke) {
            LastJoke lastJoke = (LastJoke) localPlayer.getRole().getTemplate();
            if(localPlayer.getDeathProperties().isAlive()) visible = false;
            else if(lastJoke.canUseAbility()) visible = true;
        }

        else if(localPlayer.getRole().getTemplate() instanceof FolkHero){
            FolkHero folkHero = (FolkHero) localPlayer.getRole().getTemplate();
            if(folkHero.getRemainingAbilityCount()<=0){
                visible = false;
            }
        }

        return visible;
    }




}
