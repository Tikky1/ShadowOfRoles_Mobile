package com.kankangames.shadowofroles.services;

import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.TextManager;

public final class MultiDeviceTimeService extends BaseTimeService{

    @Override
    public void toggleTimeCycle(){
        switch (time) {

            case VOTING: time = Time.NIGHT;
                break;

            case NIGHT:
                time = Time.VOTING;
                dayCount++;
                break;

        }

    }



}
