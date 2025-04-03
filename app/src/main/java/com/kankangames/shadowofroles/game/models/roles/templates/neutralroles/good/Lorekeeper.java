package com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.good;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.game.models.roles.otherinterfaces.RoleSpecificValuesChooser;
import com.kankangames.shadowofroles.game.models.roles.properties.RoleAttribute;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.game.services.BaseGameService;
import com.kankangames.shadowofroles.game.services.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Lorekeeper extends RoleTemplate implements RoleSpecificValuesChooser {
    private final Set<Player> alreadyChosenPlayers;
    private RoleTemplate guessedRole;
    private int trueGuessCount;
    public Lorekeeper() {
        super(RoleID.LORE_KEEPER, AbilityType.ACTIVE_OTHERS, RolePriority.LORE_KEEPER,
                RoleCategory.NEUTRAL_GOOD, WinningTeam.LORE_KEEPER);

        roleProperties
                .addAttribute(RoleAttribute.CAN_WIN_WITH_ANY_TEAM)
                .addAttribute(RoleAttribute.HAS_OTHER_WIN_CONDITION)
                .addAttribute(RoleAttribute.WINS_ALONE)
                .addAttribute(RoleAttribute.ROLE_BLOCK_IMMUNE);

        trueGuessCount = 0;
        alreadyChosenPlayers = new HashSet<>();
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

            String messageTemplate = textManager.getText(R.string.lore_keeper_ability_message);

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

    public Set<Player> getAlreadyChosenPlayers() {
        return alreadyChosenPlayers;
    }

    @Override
    public void chooseRoleSpecificValues(List<Player> choosablePlayers) {
        guessedRole = RoleService.getRandomRole();
        choosablePlayers.removeAll(alreadyChosenPlayers);
    }
    
}
