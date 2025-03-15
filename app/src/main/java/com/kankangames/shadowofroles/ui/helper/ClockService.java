package com.kankangames.shadowofroles.ui.helper;

import android.os.CountDownTimer;

public class ClockService {

    private CountDownTimer countDownTimer;
    private final ClockUpdateListener listener;

    public ClockService(ClockUpdateListener listener) {
        this.listener = listener;
    }

    public void startTimer(int durationMillis) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                listener.onTimeUpdate((int) millisUntilFinished);
            }

            @Override
            public void onFinish() {
                listener.onTimeUp();
            }
        };
        countDownTimer.start();
    }

    public interface ClockUpdateListener {
        void onTimeUpdate(int remainingTime);
        void onTimeUp();
    }
}
