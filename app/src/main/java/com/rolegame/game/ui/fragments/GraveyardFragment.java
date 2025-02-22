package com.rolegame.game.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rolegame.game.R;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.ui.adapters.GraveyardViewAdapter;

import java.util.List;

public class GraveyardFragment extends DialogFragment {

    private final List<Player> deadPlayers;

    public GraveyardFragment(List<Player> deadPlayers) {
        this.deadPlayers = deadPlayers;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_graveyard);

        RecyclerView graveyardView = dialog.findViewById(R.id.graveyard_recycler_view);

        graveyardView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        GraveyardViewAdapter graveyardViewAdapter = new GraveyardViewAdapter();

        graveyardViewAdapter.setDeadPlayers(deadPlayers);

        graveyardView.setAdapter(graveyardViewAdapter);

        Log.d("RecyclerView", "RecyclerView Height: " + graveyardView.getHeight());

        return dialog;
    }
}
