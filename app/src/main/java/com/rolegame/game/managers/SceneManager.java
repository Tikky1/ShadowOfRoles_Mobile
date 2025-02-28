package com.rolegame.game.managers;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.rolegame.game.R;

public class SceneManager {
    private static SceneManager instance;
    private final int[] images = {
            R.drawable.menu_night,
            R.drawable.menu_day,
            R.drawable.background0
    };

    private int currentIndex = 0;

    private final Context context;

    private SceneManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public Drawable getCurrentImage() {
        return ContextCompat.getDrawable(context, images[currentIndex]);
    }

    public Drawable nextImage() {
        currentIndex = (currentIndex + 1) % images.length;
        return ContextCompat.getDrawable(context, images[currentIndex]);
    }

    public static SceneManager getInstance(Context context){
        if(instance==null){
            instance = new SceneManager(context);
        }

        return instance;
    }
}
