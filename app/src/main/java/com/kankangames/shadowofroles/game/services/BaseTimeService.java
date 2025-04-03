package com.kankangames.shadowofroles.game.services;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.gamestate.Time;
import com.kankangames.shadowofroles.game.models.gamestate.TimePeriod;
import com.kankangames.shadowofroles.utils.managers.TextManager;

public class BaseTimeService {
    protected TimePeriod timePeriod = TimePeriod.of(Time.NIGHT, 1);

    public void toggleTimeCycle(){
        switch (timePeriod.time()) {
            case DAY:
                timePeriod.setTime(Time.VOTING);
                break;

            case VOTING:
                timePeriod.setTime(Time.NIGHT);
                break;

            case NIGHT:
                timePeriod.setTime(Time.DAY);
                timePeriod.incrementDayCount();
                break;

        }

    }

    public int getDayCount() {
        return timePeriod.dayCount();
    }


    public Time getTime() {
        return timePeriod.time();
    }

    public String getTimeAndDay(){
        TextManager textManager = TextManager.getInstance();
        String timeStr = (timePeriod.time()!=Time.NIGHT ? textManager.getText(R.string.day
        ) : textManager.getText(R.string.night));
        timeStr = String.format(timeStr, timePeriod.dayCount());
        return timeStr;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }
}
