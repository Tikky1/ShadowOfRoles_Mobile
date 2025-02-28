package com.rolegame.game.services;

import android.app.Activity;
import android.content.pm.ActivityInfo;

public interface OrientationLockService {
    default void lockOrientation(Activity activity){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
