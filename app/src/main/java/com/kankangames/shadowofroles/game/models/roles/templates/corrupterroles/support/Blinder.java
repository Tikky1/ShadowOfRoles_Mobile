package com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.support;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.game.models.roles.properties.RoleAttribute;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.game.services.BaseGameService;

import java.util.ArrayList;
import java.util.Random;

public final class Blinder extends RoleTemplate {
    public Blinder() {
        super(RoleID.BLINDER, AbilityType.OTHER_THAN_CORRUPTER,
                RolePriority.BLINDER, RoleCategory.CORRUPTER_SUPPORT, WinningTeam.CORRUPTER);

        roleProperties
                .addAttribute(RoleAttribute.KNOWS_TEAM_MEMBERS)
                .addAttribute(RoleAttribute.HAS_BLIND_ABILITY);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        String message = textManager.getText(R.string.blinder_ability_message);
        sendAbilityMessage(message,roleOwner, gameService.getMessageService());
        ArrayList<Player> players = new ArrayList<>(gameService.getAlivePlayers());

        players.remove(choosenPlayer);

        choosenPlayer.getRole().setChoosenPlayer(players.get(new Random().nextInt(players.size())));

        sendAbilityMessage(textManager.getText(R.string.got_blinded_message), choosenPlayer, gameService.getMessageService());

        return AbilityResult.SUCCESSFUL;
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(25, 10);
    }
}
