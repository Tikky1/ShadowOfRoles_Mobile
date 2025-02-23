package com.rolegame.game.managers;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.rolegame.game.R;

public class GameScreenImageManager {
    private static GameScreenImageManager instance;

    private final int[][] images = {
            {R.drawable.day0 }, // Day Images (0)
            {R.drawable.voting0 }, // Voting Images (1)
            {R.drawable.night0 }, // Night Images (2)

            {R.drawable.day_lobby_0 }, // Passing Turn Day Images (0)
            {R.drawable.voting_lobby_0 }, // Passing Turn Voting Images (1)
            {R.drawable.night_lobby_0 } // Passing Turn Night Images (2)

    };

    private final int[] indices = { 0, 0, 0, 0, 0, 0 }; // [dayIndex, votingIndex, nightIndex]
    private final Context context;

    private GameScreenImageManager(Context context) {
        this.context = context.getApplicationContext();
    }

    private Drawable nextImage(int type) {
        indices[type] = (indices[type] + 1) % images[type].length;
        return ContextCompat.getDrawable(context, images[type][indices[type]]);
    }

    public Drawable nextDayImage() {
        return nextImage(0);
    }

    public Drawable nextVotingImage() {
        return nextImage(1);
    }

    public Drawable nextNightImage() {
        return nextImage(2);
    }

    public Drawable nextDayPassingTurnImage() {
        return nextImage(3);
    }

    public Drawable nextVotingPassingTurnImage() {
        return nextImage(4);
    }

    public Drawable nextNightPassingTurnImage() {
        return nextImage(5);
    }

    public static GameScreenImageManager getInstance(Context context) {
        if (instance == null) {
            instance = new GameScreenImageManager(context);
        }
        return instance;
    }
}
