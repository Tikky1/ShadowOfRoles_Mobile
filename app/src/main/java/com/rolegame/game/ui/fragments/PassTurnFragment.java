package com.rolegame.game.ui.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rolegame.game.R;

public class PassTurnFragment extends DialogFragment {


    private OnDismissListener dismissListener;

    private ImageView fragmentBackground;

    public interface OnDismissListener {
        void onDialogDismissed();
    }

    private Drawable image;

    private String playerName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return inflater.inflate(R.layout.fragment_pass_turn, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentBackground = view.findViewById(R.id.pass_turn_background);
        fragmentBackground.setImageDrawable(image);
        TextView turnTextView = view.findViewById(R.id.pass_turn_text);
        turnTextView.setText("It's " + playerName + "'s turn" );


        Button turnPassBtn = view.findViewById(R.id.pass_turn_button);
        turnPassBtn.setOnClickListener(v -> {
            dismiss();
            if (dismissListener != null) {
                dismissListener.onDialogDismissed();
            }
        });

        hideSystemUI();

    }

    private void hideSystemUI() {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.dismissListener = listener;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setFragmentBackground(Drawable drawable) {

        image = drawable;
    }
}
