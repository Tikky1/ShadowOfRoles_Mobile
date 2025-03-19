package com.kankangames.shadowofroles.models.roles.templates.neutralroles.good;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.RoleSpecificValuesChooser;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.NeutralRole;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.services.BaseGameService;
import com.kankangames.shadowofroles.services.RoleService;

import java.util.ArrayList;
import java.util.List;

public final class Lorekeeper extends NeutralRole implements RoleSpecificValuesChooser {
    private final List<Player> alreadyChosenPlayers;
    private RoleTemplate guessedRole;
    private int trueGuessCount;
    public Lorekeeper() {
        super(RoleID.Lorekeeper, AbilityType.ACTIVE_OTHERS, RolePriority.LORE_KEEPER,
                RoleCategory.NEUTRAL_GOOD, WinningTeam.LORE_KEEPER, 0, 0, true, false);
        trueGuessCount = 0;
        alreadyChosenPlayers = new ArrayList<>();
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        if(choosenPlayer == null){
            return AbilityResult.NO_ONE_SELECTED;
        }

        if(guessedRole == null){
            return AbilityResult.NO_ROLE_SELECTED;
        }
        return executeAbility(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        alreadyChosenPlayers.add(choosenPlayer);

        if(choosenPlayer.getRole().getTemplate().getId() == guessedRole.getId()){
            trueGuessCount++;

            String messageTemplate = textManager.getText("lorekeeper_ability_message");

            String message = messageTemplate
                    .replace("{playerName}", choosenPlayer.getNameAndNumber())
                    .replace("{roleName}", choosenPlayer.getRole().getTemplate().getName());
            sendAbilityAnnouncement(message, gameService.getMessageService());
            choosenPlayer.getRole().setRevealed(true);
        }
        guessedRole = null;
        return AbilityResult.SUCCESSFUL;
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(30, 1);
    }

    public RoleTemplate getGuessedRole() {
        return guessedRole;
    }

    public void setGuessedRole(RoleTemplate guessedRole) {
        this.guessedRole = guessedRole;
    }

    public int getTrueGuessCount() {
        return trueGuessCount;
    }

    public List<Player> getAlreadyChosenPlayers() {
        return alreadyChosenPlayers;
    }

    @Override
    public boolean canWinWithOtherTeams() {
        return true;
    }

    @Override
    public void chooseRoleSpecificValues(List<Player> choosablePlayers) {
        guessedRole = RoleService.getRandomRole();
        choosablePlayers.removeAll(alreadyChosenPlayers);
    }
    
}
