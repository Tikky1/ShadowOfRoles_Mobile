package com.kankangames.shadowofroles.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.player.Player;
import com.kankangames.shadowofroles.ui.adapters.GraveyardViewAdapter;

import java.util.List;

public class GraveyardFragment extends HidingNavigationFragment {

    private final List<Player> deadPlayers;
    private View rootView;

    private Button button;

    public GraveyardFragment(List<Player> deadPlayers) {
        this.deadPlayers = deadPlayers;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_graveyard);

        rootView = dialog.findViewById(R.id.graveyard_root);
        button = dialog.findViewById(R.id.close_graveyard_button);


        backgroundTransparent(dialog);
        openingAnimation(rootView);

        button.setOnClickListener(v-> closingAnimation(rootView));

        RecyclerView graveyardView = dialog.findViewById(R.id.graveyard_recycler_view);
        graveyardView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        GraveyardViewAdapter graveyardViewAdapter = new GraveyardViewAdapter(deadPlayers, dialog.getContext());
        graveyardView.setAdapter(graveyardViewAdapter);

        return dialog;
    }


    @Override
    public int openingAnimationType() {
        return R.anim.graveyard_open;
    }

    @Override
    public int closingAnimationType() {
        return R.anim.graveyard_close;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_graveyard;
    }


}
