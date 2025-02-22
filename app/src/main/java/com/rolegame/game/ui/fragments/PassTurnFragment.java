package com.rolegame.game.ui.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rolegame.game.R;

public class PassTurnFragment extends DialogFragment {


    private OnDismissListener dismissListener;

    public interface OnDismissListener {
        void onDialogDismissed();
    }

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

        TextView turnTextView = view.findViewById(R.id.pass_turn_text);
        turnTextView.setText("It's " + playerName + "'s turn" );


        Button turnPassBtn = view.findViewById(R.id.pass_turn_button);
        turnPassBtn.setOnClickListener(v -> {
            dismiss();
            if (dismissListener != null) {
                dismissListener.onDialogDismissed();
            }
        });

    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.dismissListener = listener;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
