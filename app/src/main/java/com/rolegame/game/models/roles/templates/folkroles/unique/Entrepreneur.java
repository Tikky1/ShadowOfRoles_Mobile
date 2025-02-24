package com.rolegame.game.models.roles.templates.folkroles.unique;

import com.rolegame.game.models.player.properties.CauseOfDeath;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.abilities.AttackAbility;
import com.rolegame.game.models.roles.abilities.PriorityChangingRole;
import com.rolegame.game.models.roles.abilities.ProtectiveAbility;
import com.rolegame.game.models.roles.abilities.InvestigativeAbility;
import com.rolegame.game.models.roles.enums.*;
import com.rolegame.game.models.roles.templates.folkroles.FolkRole;
import com.rolegame.game.services.GameService;

import java.util.Random;

public class Entrepreneur extends FolkRole implements ProtectiveAbility, AttackAbility, InvestigativeAbility, PriorityChangingRole {

    private static final int HEAL_PRICE = 3;
    private static final int INFO_PRICE = 2;
    private static final int ATTACK_PRICE = 4;
    private int money;
    private ChosenAbility abilityState;
    public Entrepreneur() {
        super(RoleID.Entrepreneur, AbilityType.ACTIVE_ALL, RolePriority.NONE,
                RoleCategory.FOLK_UNIQUE, 1, 0, false);
        this.money = 5;
        this.setAbilityState(ChosenAbility.NONE);
    }

    @Override
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {

        if(gameService.getTimeService().getDayCount()>1){
            money += 2; //Passive income
        }

        return super.performAbility(roleOwner, choosenPlayer, gameService);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {

        ChosenAbility chosenAbility = abilityState;
        abilityState = ChosenAbility.NONE;

        switch (chosenAbility){

            case ATTACK: {
                if(money>= ATTACK_PRICE){
                    money -= ATTACK_PRICE;
                    return attack(roleOwner, choosenPlayer, gameService, CauseOfDeath.ENTREPRENEUR);
                }
                break;

            }
            case HEAL: {
                if(money>= HEAL_PRICE){
                    money -= HEAL_PRICE;
                    return heal(roleOwner, choosenPlayer, gameService);
                }
                break;

            }
            case INFO:{
                if(money>= INFO_PRICE){
                    money -= INFO_PRICE;
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

    public ChosenAbility getAbilityState() {
        return abilityState;
    }

    public void setAbilityState(ChosenAbility abilityState) {
        this.abilityState = abilityState;
    }

    private AbilityResult gatherInfo(Player roleOwner, Player chosenPlayer, GameService gameService){
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

    private AbilityResult insufficientMoney(Player roleOwner, GameService gameService){
        String message = languageManager.getText("entrepreneur_insufficient_money");

        switch (abilityState){
            case ATTACK: message = message
                    .replace("{abilityName}", languageManager.getText("entrepreneur_attack"));
                break;
            case HEAL:  message = message
                    .replace("{abilityName}", languageManager.getText("entrepreneur_heal"));
                break;
            case INFO:  message = message
                    .replace("{abilityName}", languageManager.getText("entrepreneur_info"));
                break;
        }
        sendAbilityMessage(message, roleOwner, gameService.getMessageService());
        return AbilityResult.INSUFFICIENT_MONEY;
    }

    @Override
    public void changePriority() {
        rolePriority = abilityState.rolePriority;
    }


    public enum ChosenAbility{
        ATTACK(RolePriority.NONE),
        HEAL(RolePriority.HEAL),
        INFO(RolePriority.NONE),
        NONE(RolePriority.NONE);

        final RolePriority rolePriority;

        ChosenAbility(RolePriority rolePriority){
            this.rolePriority = rolePriority;
        }

    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
