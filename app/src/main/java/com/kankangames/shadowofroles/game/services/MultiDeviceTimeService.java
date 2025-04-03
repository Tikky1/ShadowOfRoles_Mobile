package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.TextManager;

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
