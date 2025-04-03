package com.kankangames.shadowofroles.game.models.roles.templates.folkroles.protector;

import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.abilities.ProtectiveAbility;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.FolkRole;
import com.kankangames.shadowofroles.game.services.BaseGameService;

public final class Soulbinder extends FolkRole implements ProtectiveAbility {
    public Soulbinder() {
        super(RoleID.Soulbinder, AbilityType.ACTIVE_OTHERS, RolePriority.HEAL,
                RoleCategory.FOLK_PROTECTOR);
        roleProperties
                .setHasHealingAbility(true);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        return heal(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(20, 10);
    }
}
