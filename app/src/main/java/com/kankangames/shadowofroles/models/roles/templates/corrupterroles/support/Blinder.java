package com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.CorrupterRole;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.services.BaseGameService;

import java.util.ArrayList;
import java.util.Random;

public final class Blinder extends CorrupterRole{
    public Blinder() {
        super(RoleID.Blinder, AbilityType.OTHER_THAN_CORRUPTER, RolePriority.BLINDER, RoleCategory.CORRUPTER_SUPPORT, 0, 0,false);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        String message = textManager.getText("blinder_ability_message");
        sendAbilityMessage(message,roleOwner, gameService.getMessageService());
        ArrayList<Player> players = new ArrayList<>(gameService.getAlivePlayers());

        players.remove(choosenPlayer);

        choosenPlayer.getRole().setChoosenPlayer(players.get(new Random().nextInt(players.size())));

        sendAbilityMessage(textManager.getText("got_blinded_message"), choosenPlayer, gameService.getMessageService());

        return AbilityResult.SUCCESSFUL;
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(25, 10);
    }
}
