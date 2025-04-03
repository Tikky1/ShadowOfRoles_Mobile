package com.kankangames.shadowofroles.game.services;

import com.kankangames.shadowofroles.game.models.gamestate.Time;

public final class MultiDeviceTimeService extends BaseTimeService{

    @Override
    public void toggleTimeCycle(){
        switch (timePeriod.time()) {

            case VOTING:
                timePeriod.setTime(Time.NIGHT);
                break;

            case NIGHT:
                timePeriod.setTime(Time.VOTING);
                timePeriod.incrementDayCount();
                break;

        }

    }



}
