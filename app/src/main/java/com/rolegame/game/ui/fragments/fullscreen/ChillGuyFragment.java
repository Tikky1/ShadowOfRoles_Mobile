package com.rolegame.game.ui.fragments.fullscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rolegame.game.R;
import com.rolegame.game.models.player.Player;

public class ChillGuyFragment extends FullScreenFragment {

    private final Player chillGuyPlayer;
    public ChillGuyFragment(OnClose onClose, Player chillGuyPlayer) {
        super(onClose);
        this.chillGuyPlayer = chillGuyPlayer;
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
            onClose.backClicked();
            dismiss();
        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_chillguy;
    }

}
