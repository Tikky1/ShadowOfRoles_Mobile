package com.rolegame.game.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rolegame.game.R;
import com.rolegame.game.models.roles.templates.RoleTemplate;
import com.rolegame.game.services.GameService;
import com.rolegame.game.services.StartGameService;


public class PlayerRoleFragment extends Fragment {

    private GameService gameService;

    private RelativeLayout playerRoleInfoLayout;



    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_role_page, container, false);

        gameService = StartGameService.getInstance().getGameService();
        playerRoleInfoLayout = view.findViewById(R.id.playerRoleInfoLayout);

        setRoleInfoLayout(view);

        return view;
    }

    private void setRoleInfoLayout(View view){
        RoleTemplate currentRole = gameService.getCurrentPlayer().getRole().getTemplate();

        TextView teamText = view.findViewById(R.id.teamText);
        teamText.setText("Team: " + currentRole.getTeamText());

        TextView goalText = view.findViewById(R.id.goalText);
        goalText.setText("Goal: " + currentRole.getGoal());

        TextView abilityText = view.findViewById(R.id.abilityText);
        abilityText.setText("Abilities: " + currentRole.getAbilities());

        TextView attributesText = view.findViewById(R.id.attributesText);
        attributesText.setText("Attributes: " + currentRole.getAttributes());
    }


}


