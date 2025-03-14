package com.kankangames.shadowofroles.services;

import java.util.Timer;
import java.util.TimerTask;

public class TurnTimerService {

    private final int[] phaseDurations = new int[]{20_000, 20_000, 30_000};
    private final Timer timer = new Timer();
    private final MultiDeviceGameService multiDeviceGameService;

    public TurnTimerService(MultiDeviceGameService multiDeviceGameService) {
        this.multiDeviceGameService = multiDeviceGameService;
        schedulePhase();
    }

    private void schedulePhase() {
        int delay = getCurrentPhaseDuration();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                multiDeviceGameService.toggleDayNightCycle();
                updatePhase();
            }
        };

        timer.schedule(timerTask, delay);
    }

    private int getCurrentPhaseDuration() {
        switch (multiDeviceGameService.timeService.getTime()) {
            case DAY:
                return phaseDurations[0];
            case VOTING:
                return phaseDurations[1];
            case NIGHT:
                return phaseDurations[2];
            default:
                return 20_000;
        }
    }

    private void updatePhase() {
        schedulePhase();
    }


    public void stopTimer(){
        timer.cancel();
    }
}
