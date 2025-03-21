package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.TextManager;

public class BaseTimeService {
    protected int dayCount = 1;
    protected Time time = Time.NIGHT;

    public void toggleTimeCycle(){
        switch (time) {
            case DAY: time = Time.VOTING;
                break;

            case VOTING: time = Time.NIGHT;
                break;

            case NIGHT:
                time = Time.DAY;
                dayCount++;
                break;

        }

    }

    public int getDayCount() {
        return dayCount;
    }


    public Time getTime() {
        return time;
    }

    public String getTimeAndDay(){
        TextManager textManager = TextManager.getInstance();
        String timeStr = (time!=Time.NIGHT ? textManager.getText("day") : textManager.getText("night"));
        timeStr = String.format(timeStr, dayCount);
        return timeStr;
    }

}
