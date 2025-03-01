package com.kankangames.shadowofroles.ui.fragments.fullscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.services.FinishGameService;

public class ChillGuyFragment extends FullScreenFragment {

    private final Player chillGuyPlayer;
    private final FinishGameService finishGameService;
    public ChillGuyFragment(OnClose onClose, Player chillGuyPlayer, FinishGameService finishGameService) {
        super(onClose);
        this.chillGuyPlayer = chillGuyPlayer;
        this.finishGameService = finishGameService;
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
                finishGameService.addWinningTeam(WinningTeam.CHILL_GUY);
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
