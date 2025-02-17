package com.rolegame.game.models.roles.folkroles.protector;

import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.folkroles.FolkRole;
import com.rolegame.game.services.GameService;

public final class FolkHero extends FolkRole {

    private int abilityUseCount;

    public FolkHero() {
        super(RoleID.FolkHero, AbilityType.ACTIVE_ALL, RolePriority.IMMUNE,
                RoleCategory.FOLK_PROTECTOR, 0, 0, false);
        abilityUseCount = 0;
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return performAbilityForFolkHero(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        if(abilityUseCount<=2){
            sendAbilityMessage(LanguageManager.getText("FolkHero","abilityMessage") ,roleOwner, gameService.getMessageService());
            choosenPlayer.setImmune(true);
            abilityUseCount++;
            return AbilityResult.SUCCESSFUL;
        }
        return AbilityResult.NO_ABILITY_USE_LEFT;
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(25,1);
    }

    public int getAbilityUseCount() {
        return abilityUseCount;
    }
}
