package com.kankangames.shadowofroles.ui.fragments.fullscreen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.gamestate.WinStatus;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.game.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.game.models.gamestate.GameMode;
import com.kankangames.shadowofroles.networking.client.Client;
import com.kankangames.shadowofroles.networking.client.ClientManager;
import com.kankangames.shadowofroles.game.services.FinishGameService;

public class ChillGuyFragment extends FullScreenFragment {

    private final Player chillGuyPlayer;
    private final FinishGameService finishGameService;
    private final GameMode gameMode;
    public ChillGuyFragment(OnClose onClose, Player chillGuyPlayer, FinishGameService finishGameService, GameMode gameMode) {
        super(onClose);
        this.chillGuyPlayer = chillGuyPlayer;
        this.finishGameService = finishGameService;
        this.gameMode = gameMode;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button confirmButton = view.findViewById(R.id.chill_guy_alert_button);
        RadioButton noButton = view.findViewById(R.id.chill_guy_no_button);
        noButton.setChecked(true);
        RadioButton yesButton = view.findViewById(R.id.chill_guy_yes_button);


        confirmButton.setOnClickListener(v -> {
            boolean chillGuyWon = noButton.isChecked();

            if(gameMode == GameMode.SINGLE_DEVICE){
                chillGuyPlayer.setWinStatus(chillGuyWon ? WinStatus.WON : WinStatus.LOST);

                if(chillGuyWon){
                    finishGameService.addWinningTeam(WinningTeam.CHILL_GUY);
                }
            }
            else{
                Client client = ClientManager.getInstance().getClient();
                client.getClientGameManager().sendChillGuyInfo(chillGuyWon);
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
