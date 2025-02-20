package com.rolegame.game.models.roles.templates.neutralroles.good;

import com.rolegame.game.models.player.Player;
import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.templates.neutralroles.NeutralRole;
import com.rolegame.game.models.roles.templates.RoleTemplate;
import com.rolegame.game.services.GameService;

import java.util.ArrayList;
import java.util.List;

public final class Lorekeeper extends NeutralRole {
    private final List<Player> alreadyChosenPlayers;
    private RoleTemplate guessedRole;
    private int trueGuessCount;
    public Lorekeeper() {
        super(RoleID.Lorekeeper, AbilityType.ACTIVE_OTHERS, RolePriority.LORE_KEEPER, RoleCategory.NEUTRAL_GOOD, 0, 0, true);
        trueGuessCount = 0;
        alreadyChosenPlayers = new ArrayList<>();
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        if(choosenPlayer == null){
            return AbilityResult.NO_ONE_SELECTED;
        }

        if(guessedRole == null){
            return AbilityResult.NO_ROLE_SELECTED;
        }
        return executeAbility(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        alreadyChosenPlayers.add(choosenPlayer);

        if(choosenPlayer.getRole().getTemplate().getId() == guessedRole.getId()){
            trueGuessCount++;

            String messageTemplate = LanguageManager.getInstance().getText("lorekeeper_abilityMessage");

            String message = messageTemplate
                    .replace("{playerName}", choosenPlayer.getName())
                    .replace("{roleName}", choosenPlayer.getRole().getTemplate().getName());
            sendAbilityAnnouncement(message, gameService.getMessageService());
            choosenPlayer.setRevealed(true);
        }
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

    public void setTrueGuessCount(int trueGuessCount) {
        this.trueGuessCount = trueGuessCount;
    }

    public List<Player> getAlreadyChosenPlayers() {
        return alreadyChosenPlayers;
    }

    @Override
    public boolean canWinWithOtherTeams() {
        return true;
    }
}
