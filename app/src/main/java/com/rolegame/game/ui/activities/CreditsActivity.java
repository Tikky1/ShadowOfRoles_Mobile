package com.rolegame.game.ui.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;

import com.rolegame.game.R;

public class CreditsActivity extends ImageChangingActivity {
    private ImageView backgroundImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        backgroundImage = findViewById(R.id.credits_background_image);
        backgroundImage.setImageDrawable(sceneManager.nextImage());
    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }
}
