package com.kankangames.shadowofroles.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.managers.SceneManager;

public abstract class ImageChangingActivity extends BaseActivity {

    protected SceneManager sceneManager;

    protected final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getBooleanExtra("changeImage", false)) {

                        getBackgroundImage().setImageDrawable(sceneManager.nextImage());
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sceneManager = SceneManager.getInstance(this);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("changeImage", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    protected abstract ImageView getBackgroundImage();
}
