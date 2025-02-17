package com.rolegame.game.services;

import com.rolegame.game.gamestate.Time;

public final class TimeService {
    private int dayCount = 1;
    private Time time = Time.NIGHT;

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

}
