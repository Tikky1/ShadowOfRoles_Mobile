package com.kankangames.shadowofroles.game.models.roles.templates.corrupterroles.support;

import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityResult;
import com.kankangames.shadowofroles.game.models.roles.enums.AbilityType;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleCategory;
import com.kankangames.shadowofroles.game.models.roles.enums.RoleID;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.game.models.roles.otherinterfaces.PriorityChangingRole;
import com.kankangames.shadowofroles.game.models.roles.properties.RoleAttribute;
import com.kankangames.shadowofroles.game.models.roles.templates.folkroles.unique.Entrepreneur;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.chaos.ChillGuy;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.chaos.Clown;
import com.kankangames.shadowofroles.game.models.roles.templates.neutralroles.good.Lorekeeper;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.game.services.BaseGameService;
import com.kankangames.shadowofroles.game.services.RoleService;

import java.util.ArrayList;
import java.util.Random;

public final class Disguiser extends RoleTemplate implements PriorityChangingRole {

    private RoleTemplate currentRole;
    public Disguiser() {
        super(RoleID.DISGUISER, AbilityType.ACTIVE_ALL, RolePriority.NONE,
                RoleCategory.CORRUPTER_SUPPORT, WinningTeam.CORRUPTER);

        roleProperties
                .addAttribute(RoleAttribute.KNOWS_TEAM_MEMBERS)
                .addAttribute(RoleAttribute.HAS_DISGUISE_ABILITY);
    }

    @Override
    public AbilityResult executeAbility(Player roleOwner, Player choosenPlayer, BaseGameService gameService) {
        roleOwner.getRole().setAttack(currentRole.getRoleProperties().attack());
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

        if (currentRole.getRoleProperties().hasAttribute(RoleAttribute.ROLE_BLOCK_IMMUNE)) {
            this.getRoleProperties().addAttribute(RoleAttribute.ROLE_BLOCK_IMMUNE);
        } else {
            this.getRoleProperties().removeAttribute(RoleAttribute.ROLE_BLOCK_IMMUNE);
        }

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
