package com.kankangames.shadowofroles.game.models.message;


import androidx.annotation.NonNull;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.game.models.gamestate.TimePeriod;
import com.kankangames.shadowofroles.utils.managers.TextManager;
import com.kankangames.shadowofroles.game.models.player.Player;

public class Message {
    private final TimePeriod timePeriod;
    private final String message;
    private final Player receiver;
    private final boolean isPublic;

    public Message(TimePeriod timePeriod, String message, Player receiver, boolean isPublic) {
        this.timePeriod = timePeriod;
        this.message = message;
        this.receiver = receiver;
        this.isPublic = isPublic;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public boolean isDay() {
        return timePeriod.time() != Time.NIGHT;
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
        String timeStr = (timePeriod.time() != Time.NIGHT ?
                textManager.getText(R.string.day) : textManager.getText(R.string.night));
        timeStr = String.format(timeStr, timePeriod.dayCount());
        return timeStr;
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "timePeriod=" + timePeriod +
                ", message='" + message + '\'' +
                ", receiver=" + receiver +
                ", isPublic=" + isPublic +
                '}';
    }
}
