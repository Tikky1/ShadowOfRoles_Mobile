package com.kankangames.shadowofroles.game.models.roles.templates.folkroles.unique;

import com.kankangames.shadowofroles.game.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.abilities.AttackAbility;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.otherinterfaces.PriorityChangingRole;
import com.kankangames.shadowofroles.game.models.roles.abilities.ProtectiveAbility;
import com.kankangames.shadowofroles.game.models.roles.abilities.InvestigativeAbility;
import com.kankangames.shadowofroles.game.models.roles.otherinterfaces.RoleSpecificValuesChooser;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.FolkRole;
import com.kankangames.shadowofroles.game.services.BaseGameService;

import java.util.List;
import java.util.Random;

public class Entrepreneur extends FolkRole implements ProtectiveAbility, AttackAbility,
        InvestigativeAbility, PriorityChangingRole, RoleSpecificValuesChooser {
    private ChosenAbility chosenAbility;
    public Entrepreneur() {
        super(RoleID.Entrepreneur, AbilityType.ACTIVE_ALL, RolePriority.NONE,
                RoleCategory.FOLK_UNIQUE);
        roleProperties.setAttack(1)
                .setHasAttackAbility(true)
                .setMoney(5)
                .setHasHealingAbility(true)
                .setCanKill1v1(true);
        this.setChosenAbility(ChosenAbility.NONE);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {

        if(gameService.getTimeService().getDayCount() > 1){
            roleProperties.incrementMoney(2); //Passive income
        }

        return super.performAbility(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player chosenPlayer, BaseGameService gameService) {

        if(chosenAbility.price > roleProperties.money()){
            return insufficientMoney(roleOwner, gameService);
        }

        AbilityResult result;
        switch (chosenAbility) {
            case ATTACK:
                result = attack(roleOwner, chosenPlayer, gameService, CauseOfDeath.ENTREPRENEUR);
                break;
            case HEAL:
                result = heal(roleOwner, chosenPlayer, gameService);
                break;
            case INFO:
                result = gatherInfo(roleOwner, chosenPlayer, gameService);
                break;
            default:
                result = AbilityResult.NO_ABILITY_SELECTED;
                break;
        }

        roleProperties.decrementMoney(chosenAbility.price);

        chosenAbility = ChosenAbility.NONE;
        return result;
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(15, 1);
    }

    public ChosenAbility getChosenAbility() {
        return chosenAbility;
    }

    public void setChosenAbility(ChosenAbility chosenAbility) {
        this.chosenAbility = chosenAbility;
        this.rolePriority = chosenAbility.rolePriority;
    }

    private AbilityResult gatherInfo(Player roleOwner, Player chosenPlayer, BaseGameService gameService){
        switch (new Random().nextInt(5)){
            case 0: {
                return darkSeerAbility(roleOwner, gameService);
            }
            case 1: {
                return detectiveAbility(roleOwner, chosenPlayer, gameService);
            }
            case 2: {
                return observerAbility(roleOwner, chosenPlayer, gameService);
            }
            case 3: {
                return stalkerAbility(roleOwner, chosenPlayer, gameService);
            }
            default: {
                return darkRevealerAbility(roleOwner, chosenPlayer, gameService);
            }
        }
    }

    private AbilityResult insufficientMoney(Player roleOwner, BaseGameService gameService){
        String message = textManager.getText("money_insufficient");

        switch (chosenAbility){
            case ATTACK: message = message
                    .replace("{abilityName}", textManager.getText("entrepreneur_attack"));
                break;
            case HEAL:  message = message
                    .replace("{abilityName}", textManager.getText("entrepreneur_heal"));
                break;
            case INFO:  message = message
                    .replace("{abilityName}", textManager.getText("entrepreneur_info"));
                break;
        }
        sendAbilityMessage(message, roleOwner, gameService.getMessageService());
        return AbilityResult.INSUFFICIENT_MONEY;
    }

    @Override
    public void changePriority() {
        rolePriority = chosenAbility.rolePriority;
    }

    @Override
    public void chooseRoleSpecificValues(List<Player> choosablePlayers) {
        boolean randBool = new Random().nextBoolean();
        chosenAbility = randBool ? Entrepreneur.ChosenAbility.HEAL : Entrepreneur.ChosenAbility.ATTACK;
    }

    public enum ChosenAbility{
        ATTACK(RolePriority.NONE, 4),
        HEAL(RolePriority.HEAL,3),
        INFO(RolePriority.NONE,2),
        NONE(RolePriority.NONE,0);

        final RolePriority rolePriority;
        final int price;

        ChosenAbility(RolePriority rolePriority , int price){
            this.rolePriority = rolePriority;

            this.price = price;
        }

        public int getPrice(){return price;}

    }

}
