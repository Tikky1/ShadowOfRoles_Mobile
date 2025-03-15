package com.kankangames.shadowofroles.networking.jsonobjects;

import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;

public class PlayerInfo {
    private final int senderPlayerNumber;
    private final int chosenPlayerNumber;
    private final RoleTemplate senderRole;

    public PlayerInfo(int senderPlayerNumber, int chosenPlayerNumber, RoleTemplate senderRole) {
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
