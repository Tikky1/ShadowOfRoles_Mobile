package com.kankangames.shadowofroles.ui.activities;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kankangames.shadowofroles.R;

public class CreditsActivity extends ImageChangingActivity {
    private ImageView backgroundImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        backgroundImage = findViewById(R.id.credits_background_image);
        backgroundImage.setImageDrawable(sceneManager.nextImage());

        TextView creditsText = findViewById(R.id.credits_text);
        Animation creditsAnimation = AnimationUtils.loadAnimation(this, R.anim.credits_animation);
        creditsText.startAnimation(creditsAnimation);
    }

    @Override
    protected ImageView getBackgroundImage() {
        return backgroundImage;
    }
}
