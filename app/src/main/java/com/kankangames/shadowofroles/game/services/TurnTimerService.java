package com.kankangames.shadowofroles.game.services;

import com.kankangames.shadowofroles.game.models.gamestate.TimeManager;

import java.util.Timer;
import java.util.TimerTask;

public final class TurnTimerService {

    private Timer timer = new Timer();
    private final MultiDeviceGameService multiDeviceGameService;
    private boolean isRunning = true;
    private final OnTimeChangeListener onTimeChangeListener;

    public TurnTimerService(final MultiDeviceGameService multiDeviceGameService, OnTimeChangeListener onTimeChangeListener) {
        this.multiDeviceGameService = multiDeviceGameService;
        this.onTimeChangeListener = onTimeChangeListener;
        schedulePhase();
    }

    private void schedulePhase() {
        if (!isRunning) return;

        int delay = getCurrentPhaseDuration();

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!isRunning) return;

                multiDeviceGameService.toggleDayNightCycle();

                if(onTimeChangeListener!=null){
                    onTimeChangeListener.onTimeChange();
                }

                updatePhase();
            }
        };

        timer.schedule(timerTask, delay);
    }

    private int getCurrentPhaseDuration() {
        int delay = 500;
        switch (multiDeviceGameService.timeService.getTime()) {
            case DAY:
                return TimeManager.dayTime + delay;
            case VOTING:
                return TimeManager.votingTime + delay;
            case NIGHT:
                return TimeManager.nightTime + delay;
            default:
                return 20_000 + delay;
        }
    }

    private void updatePhase() {
        schedulePhase();
    }

    public void stopTimer() {
        isRunning = false;
        timer.cancel();
        timer.purge();
    }

    public interface OnTimeChangeListener{
        void onTimeChange();
    }
}
