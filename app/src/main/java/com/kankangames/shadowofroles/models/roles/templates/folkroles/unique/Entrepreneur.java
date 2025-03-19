package com.kankangames.shadowofroles.models.roles.templates.folkroles.unique;

import com.kankangames.shadowofroles.models.player.properties.CauseOfDeath;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.AttackAbility;
import com.kankangames.shadowofroles.models.roles.abilities.PriorityChangingRole;
import com.kankangames.shadowofroles.models.roles.abilities.ProtectiveAbility;
import com.kankangames.shadowofroles.models.roles.abilities.InvestigativeAbility;
import com.kankangames.shadowofroles.models.roles.abilities.RoleSpecificValuesChooser;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.FolkRole;
import com.kankangames.shadowofroles.services.BaseGameService;

import java.util.List;
import java.util.Random;

public class Entrepreneur extends FolkRole implements ProtectiveAbility, AttackAbility,
        InvestigativeAbility, PriorityChangingRole, RoleSpecificValuesChooser {
    private int money;
    private ChosenAbility chosenAbility;
    public Entrepreneur() {
        super(RoleID.Entrepreneur, AbilityType.ACTIVE_ALL, RolePriority.NONE,
                RoleCategory.FOLK_UNIQUE, 1, 0, false);
        this.money = 5;
        this.setChosenAbility(ChosenAbility.NONE);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {

        if(gameService.getTimeService().getDayCount()>1){
            money += 2; //Passive income
        }

        return super.performAbility(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {

        ChosenAbility chosenAbility = this.chosenAbility;
        this.chosenAbility = ChosenAbility.NONE;

        switch (chosenAbility){

            case ATTACK: {
                if(money>= ChosenAbility.ATTACK.price){
                    money -= ChosenAbility.ATTACK.price;
                    return attack(roleOwner, choosenPlayer, gameService, CauseOfDeath.ENTREPRENEUR);
                }
                break;

            }
            case HEAL: {
                if(money>= ChosenAbility.HEAL.price){
                    money -= ChosenAbility.HEAL.price;
                    return heal(roleOwner, choosenPlayer, gameService);
                }
                break;

            }
            case INFO:{
                if(money>= ChosenAbility.INFO.price){
                    money -= ChosenAbility.INFO.price;
                    return gatherInfo(roleOwner, choosenPlayer, gameService);
                }
                break;

            }
            default: {
                return AbilityResult.NO_ABILITY_SELECTED;
            }
        }
        return insufficientMoney(roleOwner, gameService);
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
        String message = textManager.getText("entrepreneur_insufficient_money");

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

    public int getMoney() {
        return money;
    }

}
