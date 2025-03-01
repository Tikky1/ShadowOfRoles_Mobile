package com.kankangames.shadowofroles.models.roles.templates.folkroles.protector;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.FolkRole;
import com.kankangames.shadowofroles.services.GameService;

public final class FolkHero extends FolkRole {

    private int remainingAbilityCount;

    public FolkHero() {
        super(RoleID.FolkHero, AbilityType.ACTIVE_ALL, RolePriority.IMMUNE,
                RoleCategory.FOLK_PROTECTOR, 0, 0, false);
        remainingAbilityCount = 2;
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        return performAbilityForFolkHero(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        if(remainingAbilityCount > 0){
            sendAbilityMessage(languageManager.getText("folkhero_ability_message") ,roleOwner, gameService.getMessageService());
            choosenPlayer.setImmune(true);
            remainingAbilityCount--;
            return AbilityResult.SUCCESSFUL;
        }
        return AbilityResult.NO_ABILITY_USE_LEFT;
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(25, 1);
    }

    public int getRemainingAbilityCount() {
        return remainingAbilityCount;
    }
}
