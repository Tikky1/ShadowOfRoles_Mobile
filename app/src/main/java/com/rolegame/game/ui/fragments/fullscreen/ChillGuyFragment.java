package com.rolegame.game.ui.fragments.fullscreen;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rolegame.game.R;
import com.rolegame.game.models.player.Player;

public class ChillGuyFragment extends FullScreenFragment {

    private final Player chillGuyPlayer;
    private ClickOnButton clickOnButton;
    public ChillGuyFragment(Player chillGuyPlayer) {
        this.chillGuyPlayer = chillGuyPlayer;
    }

    public interface ClickOnButton{
        void buttonClicked();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button confirmButton = view.findViewById(R.id.chill_guy_alert_button);
        RadioButton noButton = view.findViewById(R.id.chill_guy_no_button);
        noButton.setChecked(true);
        RadioButton yesButton = view.findViewById(R.id.chill_guy_yes_button);


        confirmButton.setOnClickListener(v -> {
            if(noButton.isChecked()){
                chillGuyPlayer.setHasWon(true);
            }
            clickOnButton.buttonClicked();
            dismiss();
        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_chillguy;
    }

    public void setClickOnButton(ClickOnButton clickOnButton) {
        this.clickOnButton = clickOnButton;
    }
}
