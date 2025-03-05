package com.kankangames.shadowofroles.models;


import androidx.annotation.NonNull;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.models.player.Player;

public class Message {
    private final int dayCount;
    private final boolean isDay;
    private final String message;
    private final Player receiver;
    private final boolean isPublic;

    public Message(int dayCount, boolean isDay, String message, Player receiver, boolean isPublic) {
        this.dayCount = dayCount;
        this.isDay = isDay;
        this.message = message;
        this.receiver = receiver;
        this.isPublic = isPublic;
    }

    public int getDayCount() {
        return dayCount;
    }

    public boolean isDay() {
        return isDay;
    }

    public String getMessage() {
        return message;
    }

    public Player getReceiver() {
        return receiver;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getTimeAndDayCountAsString(){
        TextManager textManager = TextManager.getInstance();
        String timeStr = (isDay ? textManager.getText("day") : textManager.getText("night"));
        timeStr = String.format(timeStr, dayCount);
        return timeStr;
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "dayCount=" + dayCount +
                ", isDay=" + isDay +
                ", message='" + message + '\'' +
                '}';
    }
}
