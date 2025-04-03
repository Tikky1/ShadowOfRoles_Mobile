package com.kankangames.shadowofroles.game.models.roles.templates.folkroles.protector;

import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.FolkRole;
import com.kankangames.shadowofroles.game.services.BaseGameService;

public final class FolkHero extends FolkRole {

    public FolkHero() {
        super(RoleID.FolkHero, AbilityType.ACTIVE_ALL, RolePriority.IMMUNE,
                RoleCategory.FOLK_PROTECTOR);
        roleProperties.setHasImmuneAbility(true)
                .setAbilityUsesLeft(2);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return performAbilityForFolkHero(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        if(roleProperties.abilityUsesLeft() > 0){
            sendAbilityMessage(textManager.getText("folkhero_ability_message") ,roleOwner, gameService.getMessageService());
            choosenPlayer.getRole().setImmune(true);
            roleProperties.decrementAbilityUsesLeft();
            return AbilityResult.SUCCESSFUL;
        }
        return AbilityResult.NO_ABILITY_USE_LEFT;
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(25, 1);
    }

}
