package com.kankangames.shadowofroles.models.roles.templates;

import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.RoleProperties;
import com.kankangames.shadowofroles.models.roles.abilities.PerformAbility;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.services.BaseGameService;
import com.kankangames.shadowofroles.services.MessageService;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class RoleTemplate implements PerformAbility, Serializable {

    protected final RoleID id;
    protected final RoleProperties roleProperties;
    protected final RoleCategory roleCategory;
    protected final WinningTeam winningTeam;
    protected transient RolePriority rolePriority;
    protected AbilityType abilityType;

    protected transient final TextManager textManager = TextManager.getInstance();

    public RoleTemplate(RoleID id, AbilityType abilityType, RolePriority rolePriority, RoleCategory roleCategory,
                        WinningTeam winningTeam) {
        // IMPORTANT! When adding a new role template, the role id and role name in the lang json files must be the same!
        this.id = id;
        this.roleProperties = new RoleProperties();
        this.abilityType = abilityType;
        this.rolePriority = rolePriority;
        this.roleCategory = roleCategory;
        this.winningTeam = winningTeam;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoleTemplate that = (RoleTemplate) o;
        return id == that.id;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(id);
    }

    public final RoleTemplate copy() {
        try {
            return this.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create copy of Role", e);
        }
    }

    public <T extends RoleTemplate> T castRole(Class<T> clazz){

        if(clazz.isInstance(this)){
            return clazz.cast(this);
        }
        throw new ClassCastException("Cannot cast " + this.getClass().getSimpleName() + " to " + clazz.getSimpleName());

    }


    public List<Player> filterChoosablePlayers(Player roleOwner, List<Player> players){
        return players.stream().filter(player ->
                        roleOwner.getRole().getTemplate().getAbilityType().canUseAbility(roleOwner,player)).
                collect(Collectors.toList());
    }

    public abstract AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService);
    public AbilityResult performAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService){
        return defaultPerformAbility(roleOwner,choosenPlayer,gameService);
    }

    protected final void sendAbilityMessage(String message, Player receiver, MessageService messageService){
        messageService.sendAbilityMessage(message, receiver);
    }

    protected final void sendAbilityAnnouncement(String message, MessageService messageService){
        messageService.sendAbilityAnnouncement(message);
    }

    public final RoleID getId() {
        return id;
    }

    public final String getName() {
        return textManager.getTextSuffix(id.name(),"name");
    }

    public final String getAttributes() {
        return textManager.getTextSuffix(id.name(), "attributes");
    }

    public final String getAbilities() {
        return textManager.getTextSuffix(id.name(),"abilities");
    }

    public abstract String getGoal();

    public final RolePriority getRolePriority() {
        return rolePriority;
    }

    public final WinningTeam getWinningTeam() {
        return winningTeam;
    }

    public final String getTeamText(){
        return textManager.getTextPrefix(winningTeam.getTeam().name(),"team");
    }

    public final RoleCategory getRoleCategory() {
        return roleCategory;
    }

    public final void setRolePriority(RolePriority rolePriority) {
        this.rolePriority = rolePriority;
    }

    public final AbilityType getAbilityType() {
        return abilityType;
    }

    public final void setAbilityType(AbilityType abilityType) {
        this.abilityType = abilityType;
    }

    public RoleProperties getRoleProperties() {
        return roleProperties;
    }

    public abstract ChanceProperty getChanceProperty();

    public static class ChanceProperty {
        private final int chance;
        private final int maxNumber;

        public ChanceProperty(int chance, int maxNumber) {
            if (chance <= 0) {
                throw new IllegalArgumentException("Chance must be positive");
            }
            if (maxNumber <= 0) {
                throw new IllegalArgumentException("Max number must be positive");
            }
            this.chance = chance;
            this.maxNumber = maxNumber;
        }

        public int getChance() {
            return chance;
        }

        public int getMaxNumber() {
            return maxNumber;
        }

        @Override
        public String toString() {
            return "ChanceProperty{" +
                    "chance=" + chance +
                    ", maxNumber=" + maxNumber +
                    '}';
        }
    }

}
