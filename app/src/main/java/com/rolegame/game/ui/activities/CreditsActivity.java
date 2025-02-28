package com.rolegame.game.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rolegame.game.R;
import com.rolegame.game.services.OrientationLockService;

public class CreditsActivity extends BaseActivity implements OrientationLockService {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lockOrientation(this);

        setContentView(R.layout.activity_credits);

        

    }
}
