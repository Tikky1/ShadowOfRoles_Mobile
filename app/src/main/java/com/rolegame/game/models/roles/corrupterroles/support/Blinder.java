package com.rolegame.game.models.roles.corrupterroles.support;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.roles.corrupterroles.CorrupterRole;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.services.GameService;

import java.util.ArrayList;
import java.util.Random;

public final class Blinder extends CorrupterRole{
    public Blinder() {
        super(RoleID.Blinder, AbilityType.OTHER_THAN_CORRUPTER, RolePriority.BLINDER, RoleCategory.CORRUPTER_SUPPORT, 0, 0,false);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        String message = LanguageManager.getText("Blinder","abilityMessage");
        sendAbilityMessage(message,roleOwner, gameService.getMessageService());
        ArrayList<Player> players = new ArrayList<>(gameService.getAlivePlayers());

        players.remove(choosenPlayer);

        choosenPlayer.getRole().setChoosenPlayer(players.get(new Random().nextInt(players.size())));

        sendAbilityMessage(LanguageManager.getText("Blinder","blindMessage"), choosenPlayer, gameService.getMessageService());

        return AbilityResult.SUCCESSFUL;
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(25,10);
    }
}
