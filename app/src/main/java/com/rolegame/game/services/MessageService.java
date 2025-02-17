package com.rolegame.game.services;

import com.rolegame.game.managers.LanguageManager;
import com.rolegame.game.models.Message;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.enums.RoleID;
import com.rolegame.game.models.roles.enums.RolePriority;

import java.util.LinkedList;

public final class MessageService {
    private final GameService gameService;
    private final LinkedList<Message> messages = new LinkedList<>();

    MessageService(GameService gameService){
        this.gameService = gameService;
    }
    public void resetMessages() {
        messages.clear();
    }


    public void sendMessage(String message, Player receiver, boolean isPublic, boolean isDay) {
        messages.add(new Message(gameService.getTimeService().getDayCount(),
                isDay, message, receiver, isPublic));
    }

    private void createNightMessage(String message, Player receiver, boolean isPublic, boolean isDay) {
         messages.add(new Message(gameService.getTimeService().getDayCount()-1,
                isDay, message, receiver, isPublic));
    }

    public void sendMessage(String message, Player receiver, boolean isPublic, boolean isDay, int dayCount) {
        messages.add(new Message(dayCount, isDay, message, receiver, isPublic));
    }

    /**
     * Send messages to exceptional situations like folk hero's target is role blocked
     * @param alivePlayer any player
     */
    void sendSpecificRoleMessages(final Player alivePlayer){

        if(alivePlayer.getRole().getTemplate().isRoleBlockImmune() && !alivePlayer.getRole().isCanPerform()
                && !alivePlayer.isImmune()){
            createNightMessage(LanguageManager.getText("RoleBlock","RBimmuneMessage"),
                    alivePlayer, false, false);
        }

        if(alivePlayer.getRole().getChoosenPlayer()==null){
            return;
        }
        if(alivePlayer.getRole().getChoosenPlayer().isImmune() &&
                alivePlayer.getRole().getTemplate().getRolePriority().getPriority()<= RolePriority.ROLE_BLOCK.getPriority()
        && alivePlayer.getRole().getTemplate().getId() != RoleID.FolkHero){
            createNightMessage(LanguageManager.getText("RoleBlock","immuneMessage"),
                    alivePlayer, false, false);
        }
    }

    public void sendAbilityMessage(String message, Player receiver){
        createNightMessage(message, receiver, false, false);
    }

    public void sendAbilityAnnouncement(String message){
        createNightMessage(message, null, true, false);
    }


    public LinkedList<Message> getMessages() {
        return messages;
    }

}
