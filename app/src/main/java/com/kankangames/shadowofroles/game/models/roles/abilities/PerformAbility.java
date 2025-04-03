package com.kankangames.shadowofroles.game.models.roles.abilities;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.utils.managers.TextManager;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.services.BaseGameService;

public interface PerformAbility {

    /**
     *
     * @param roleOwner
     * @param choosenPlayer
     * @param gameService
     * @return
     */
    default AbilityResult defaultPerformAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService){
        if(!roleOwner.getRole().isCanPerform()&&!roleOwner.getRole().isImmune()){
            gameService.getMessageService().sendAbilityMessage(TextManager.getInstance().getText(R.string.role_blocked_message),roleOwner);
            return AbilityResult.ROLE_BLOCKED;
        }

        if(choosenPlayer==null){
            return AbilityResult.NO_ONE_SELECTED;
        }

        if(choosenPlayer.getRole().isImmune()){
            return AbilityResult.TARGET_IMMUNE;
        }

        return roleOwner.getRole().getTemplate().executeAbility(roleOwner, choosenPlayer, gameService);
    }

    /**
     *
     * @param roleOwner
     * @param gameService
     * @return
     */
    default AbilityResult performAbilityForPassiveRoles(Player roleOwner, BaseGameService gameService){
        if(!roleOwner.getRole().isCanPerform()&&!roleOwner.getRole().isImmune()){
            gameService.getMessageService().sendAbilityMessage(TextManager.getInstance().getText(R.string.role_blocked_message),roleOwner);
            return AbilityResult.ROLE_BLOCKED;
        }
        return roleOwner.getRole().getTemplate().executeAbility(roleOwner, null, gameService);
    }

    /**
     *
     * @param roleOwner
     * @param choosenPlayer
     * @param gameService
     * @return
     */
    default AbilityResult performAbilityForBlockImmuneRoles(Player roleOwner, Player choosenPlayer, BaseGameService gameService){

        if(roleOwner.getRole().getChoosenPlayer()==null){
            return AbilityResult.NO_ONE_SELECTED;
        }

        if(choosenPlayer.getRole().isImmune()){
            return AbilityResult.TARGET_IMMUNE;
        }
        return roleOwner.getRole().getTemplate().executeAbility(roleOwner, choosenPlayer, gameService);
    }

    /**
     *
     * @return
     */
    default AbilityResult performAbilityForNoAbilityRoles(){
        return AbilityResult.NO_ABILITY_EXIST;
    }

    /**
     *
     * @param roleOwner
     * @param choosenPlayer
     * @param gameService
     * @return
     */
    default AbilityResult performAbilityForFolkHero(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {

        if(choosenPlayer==null){
            return AbilityResult.NO_ONE_SELECTED;
        }

        if(!roleOwner.getRole().isCanPerform()){
            gameService.getMessageService().sendAbilityMessage(TextManager.getInstance().getText(R.string.role_blocked_message) ,roleOwner);
            return AbilityResult.ROLE_BLOCKED;
        }

        return roleOwner.getRole().getTemplate().executeAbility(roleOwner, choosenPlayer, gameService);
    }

}
