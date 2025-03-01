package com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support;

import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.abilities.PriorityChangingRole;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.CorrupterRole;
import com.kankangames.shadowofroles.models.roles.enums.*;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.unique.Entrepreneur;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.chaos.ChillGuy;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.chaos.Clown;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.good.Lorekeeper;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.services.GameService;
import com.kankangames.shadowofroles.services.RoleService;

import java.util.ArrayList;
import java.util.Random;

public final class Disguiser extends CorrupterRole implements PriorityChangingRole {

    private RoleTemplate currentRole;
    public Disguiser() {
        super(RoleID.Disguiser, AbilityType.ACTIVE_ALL, RolePriority.NONE, RoleCategory.CORRUPTER_SUPPORT, 0, 0, false);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, GameService gameService) {
        roleOwner.setAttack(currentRole.getAttack());
        return currentRole.executeAbility(roleOwner, choosenPlayer, gameService);
    }

    private void setRandomRole(){
        ArrayList<RoleTemplate> possibleRoles = new ArrayList<>(RoleService.getAllRoles());
        possibleRoles.remove(new Disguiser());
        possibleRoles.remove(new Entrepreneur());
        possibleRoles.remove(new ChillGuy());
        possibleRoles.remove(new Lorekeeper());
        possibleRoles.remove(new Clown());
        possibleRoles.remove(new LastJoke());

        currentRole = possibleRoles.get(new Random().nextInt(possibleRoles.size())).copy();

        this.setRolePriority(currentRole.getRolePriority());
        this.setRoleBlockImmune(currentRole.isRoleBlockImmune());

    }

    @Override
    public void changePriority() {
        setRandomRole();
    }

    @Override
    public ChanceProperty getChanceProperty() {
        return new ChanceProperty(15, 10);
    }


}
