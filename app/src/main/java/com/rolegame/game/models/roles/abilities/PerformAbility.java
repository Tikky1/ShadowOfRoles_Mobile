package com.rolegame.game.models.roles.abilities;

import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.AbilityResult;
import com.rolegame.game.services.GameService;

public interface PerformAbility {

    /**
     *
     * @param roleOwner
     * @param choosenPlayer
     * @param gameService
     * @return
     */
    default AbilityResult defaultPerformAbility(Player roleOwner, Player choosenPlayer, GameService gameService){
        if(!roleOwner.getRole().isCanPerform()&&!roleOwner.isImmune()){
            gameService.getMessageService().sendAbilityMessage(LanguageManager.getText("RoleBlock","roleBlockedMessage"),roleOwner);
            return AbilityResult.ROLE_BLOCKED;
        }

        if(choosenPlayer==null){
            return AbilityResult.NO_ONE_SELECTED;
        }

        if(choosenPlayer.isImmune()){
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
    default AbilityResult performAbilityForPassiveRoles(Player roleOwner, GameService gameService){
        if(!roleOwner.getRole().isCanPerform()&&!roleOwner.isImmune()){
            gameService.getMessageService().sendAbilityMessage(LanguageManager.getText("RoleBlock","roleBlockedMessage"),roleOwner);
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
    default AbilityResult performAbilityForBlockImmuneRoles(Player roleOwner, Player choosenPlayer, GameService gameService){

        if(roleOwner.getRole().getChoosenPlayer()==null){
            return AbilityResult.NO_ONE_SELECTED;
        }

        if(choosenPlayer.isImmune()){
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
    default AbilityResult performAbilityForFolkHero(Player roleOwner, Player choosenPlayer, GameService gameService) {

        if(choosenPlayer==null){
            return AbilityResult.NO_ONE_SELECTED;
        }

        if(!roleOwner.getRole().isCanPerform()){
            gameService.getMessageService().sendAbilityMessage(LanguageManager.getText("RoleBlock","roleBlockedMessage") ,roleOwner);
            return AbilityResult.ROLE_BLOCKED;
        }

        return roleOwner.getRole().getTemplate().executeAbility(roleOwner, choosenPlayer, gameService);
    }

}
