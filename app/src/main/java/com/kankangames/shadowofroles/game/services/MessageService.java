package com.kankangames.shadowofroles.game.services;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.game.models.gamestate.TimePeriod;
import com.kankangames.shadowofroles.game.models.roles.properties.RoleAttribute;
import com.kankangames.shadowofroles.utils.managers.TextManager;
import com.kankangames.shadowofroles.game.models.message.Message;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.RolePriority;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class MessageService {
    private final BaseGameService gameService;
    private final Map<TimePeriod, List<Message>> messages =
            new TreeMap<>(Comparator.comparing(timePeriod -> ((TimePeriod) timePeriod).dayCount())
                    .thenComparing(timePeriod -> ((TimePeriod) timePeriod).time()));

    MessageService(BaseGameService gameService){
        this.gameService = gameService;
    }
    public void resetMessages() {
        messages.clear();
    }


    public void sendMessage(String message, Player receiver, boolean isPublic) {

        TimePeriod messageTimePeriod = getMessageTimePeriod(gameService.timeService.timePeriod);
        List<Message> messageList = messages.computeIfAbsent(messageTimePeriod, k -> new ArrayList<>());
        messageList.add(new Message(messageTimePeriod, message, receiver, isPublic));
    }


    private TimePeriod getMessageTimePeriod(TimePeriod currentPeriod){
        if(currentPeriod.time() == Time.DAY){
            return new TimePeriod(Time.NIGHT, currentPeriod.dayCount() - 1);
        } else if (currentPeriod.time() == Time.NIGHT) {
            return new TimePeriod(Time.VOTING, currentPeriod.dayCount());
        }

        return currentPeriod;
    }


    private static TimePeriod getAnnouncementPeriod(TimePeriod currentPeriod){
        switch (currentPeriod.time()){

            case NIGHT:
                return new TimePeriod(Time.VOTING, currentPeriod.dayCount());

            case DAY:
                return new TimePeriod(Time.NIGHT, currentPeriod.dayCount()-1);

            default:
                return currentPeriod;
        }

    }

    /**
     * Send messages to exceptional situations like folk hero's target is role blocked
     * @param alivePlayer any player
     */
    void sendSpecificRoleMessages(final Player alivePlayer){

        if(alivePlayer.getRole().getTemplate().getRoleProperties().hasAttribute(RoleAttribute.ROLE_BLOCK_IMMUNE) && !alivePlayer.getRole().isCanPerform()
                && !alivePlayer.getRole().isImmune()){
            sendMessage(TextManager.getInstance().getText(R.string.rb_immune_message),
                    alivePlayer, false);
        }

        if(alivePlayer.getRole().getChoosenPlayer()==null){
            return;
        }
        if(alivePlayer.getRole().getChoosenPlayer().getRole().isImmune() &&
                alivePlayer.getRole().getTemplate().getRolePriority().getPriority() <= RolePriority.ROLE_BLOCK.getPriority()
        && !alivePlayer.getRole().getTemplate().getRoleProperties().hasAttribute(RoleAttribute.HAS_IMMUNE_ABILITY)){
            sendMessage(TextManager.getInstance().getText(R.string.immune_message),
                    alivePlayer, false);
        }
    }

    public Map<TimePeriod, List<Message>> getPlayerMessages(Player player){
        Map<TimePeriod, List<Message>> sendMap =
                new TreeMap<>(Comparator.comparing(timePeriod -> ((TimePeriod) timePeriod).dayCount())
                        .thenComparing(timePeriod -> ((TimePeriod) timePeriod).time()));
        for (Map.Entry<TimePeriod, List<Message>> entry: messages.entrySet()){
            sendMap.put(entry.getKey(), entry.getValue().stream().filter(
                    message -> message.isPublic() || message.getReceiver().getNumber() == player.getNumber()
            ).collect(Collectors.toList()));
        }
        return sendMap;

    }
    public Map<TimePeriod, List<Message>> getDailyAnnouncements(){
        return getDailyAnnouncements(messages, gameService.timeService.timePeriod);
    }

    public static Map<TimePeriod, List<Message>> getDailyAnnouncements(Map<TimePeriod, List<Message>> messages, TimePeriod currentPeriod){
        Map<TimePeriod, List<Message>> sendMap =
                new HashMap<>();
        TimePeriod timePeriod = getAnnouncementPeriod(currentPeriod);
        List<Message> messageList = messages.getOrDefault(timePeriod, new ArrayList<>())
                .stream()
                .filter(Message::isPublic)
                .collect(Collectors.toList());
        sendMap.put(timePeriod, messageList);
        return sendMap;
    }
    public void sendAbilityMessage(String message, Player receiver){
        sendMessage(message, receiver, false);
    }

    public void sendAbilityAnnouncement(String message){
        sendMessage(message, null, true);
    }

}
