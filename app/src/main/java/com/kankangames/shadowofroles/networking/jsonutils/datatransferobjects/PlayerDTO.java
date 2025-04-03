package com.kankangames.shadowofroles.networking.jsonutils.datatransferobjects;

import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;

public class PlayerDTO {
    private final int senderPlayerNumber;
    private final int chosenPlayerNumber;
    private final RoleTemplate senderRole;

    public PlayerDTO(int senderPlayerNumber, int chosenPlayerNumber, RoleTemplate senderRole) {
        this.senderPlayerNumber = senderPlayerNumber;
        this.chosenPlayerNumber = chosenPlayerNumber;
        this.senderRole = senderRole;
    }

    public int getSenderPlayerNumber() {
        return senderPlayerNumber;
    }

    public int getChosenPlayerNumber() {
        return chosenPlayerNumber;
    }

    public RoleTemplate getSenderRole() {
        return senderRole;
    }
}
