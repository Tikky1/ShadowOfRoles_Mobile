package com.rolegame.game.ui.fragments;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rolegame.game.R;
import com.rolegame.game.gamestate.Time;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.templates.RoleTemplate;
import com.rolegame.game.models.roles.templates.folkroles.protector.FolkHero;
import com.rolegame.game.services.GameService;
import com.rolegame.game.services.RoleService;
import com.rolegame.game.services.StartGameService;
import com.rolegame.game.ui.adapters.LorekeeperAdapter;


public class PlayerRoleFragment extends Fragment {

    private GameService gameService;
    private Time time;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_role_page, container, false);

        gameService = StartGameService.getInstance().getGameService();
        setRoleInfoLayout(view);

        time = gameService.getTimeService().getTime();

        switch (gameService.getCurrentPlayer().getRole().getTemplate().getId()){
            case Lorekeeper:

                setLoreKeeperInfo(view, inflater);
                break;

            case Entrepreneur:
                setEntrepreneurInfo();
                break;

            case FolkHero:
                setFolkHeroInfo(view, inflater);
                break;

        }


        return view;
    }



    private void setRoleInfoLayout(View view) {
        RoleTemplate currentRole = gameService.getCurrentPlayer().getRole().getTemplate();

        TextView teamText = view.findViewById(R.id.all_team_text);
        TextView abilityText = view.findViewById(R.id.all_ability_text);
        TextView attributesText = view.findViewById(R.id.all_attributes_text);
        TextView goalText = view.findViewById(R.id.all_goal_text);
        TextView roleText = view.findViewById(R.id.all_role_text);

        roleText.setText(currentRole.getName());
        teamText.setText(currentRole.getTeamText());
        abilityText.setText(currentRole.getAbilities());
        attributesText.setText(currentRole.getAttributes());
        goalText.setText(currentRole.getGoal());
    }

    private void setLoreKeeperInfo(View view, LayoutInflater inflater){
        FrameLayout spinnerContainer = view.findViewById(R.id.unique_roles_layout);
        ViewGroup spinnerBox = (ViewGroup) inflater.inflate(R.layout.lore_keeper_box, spinnerContainer, true);
        Spinner spinner = spinnerBox.findViewById(R.id.lorekeeper_spinner);
        LorekeeperAdapter lorekeeperAdapter = new LorekeeperAdapter(view.getContext(), RoleService.getAllRoles());
        spinner.setAdapter(lorekeeperAdapter);
    }

    private void setEntrepreneurInfo(){

    }

    private void setFolkHeroInfo(View view, LayoutInflater inflater){
        Player currentPlayer = gameService.getCurrentPlayer();
        FolkHero folkHero = (FolkHero) currentPlayer.getRole().getTemplate();

        FrameLayout spinnerContainer = view.findViewById(R.id.unique_roles_layout);
        ViewGroup folkHeroBox = (ViewGroup) inflater.inflate(R.layout.folk_hero_box, spinnerContainer, true);
        TextView currentText = folkHeroBox.findViewById(R.id.folk_hero_this_night_text_view);
        TextView nextText = folkHeroBox.findViewById(R.id.folk_hero_next_night_text);
        TextView selectedPlayerText = folkHeroBox.findViewById(R.id.folk_hero_selected_player);

        int remainingAbilityCount = folkHero.getRemainingAbilityCount();
        currentText.setText("Remaining Ability Count: " + remainingAbilityCount);


        boolean isAbilityUsed = currentPlayer.getRole().getChoosenPlayer() !=null;
        nextText.setText("Expected Ability Count: " +
                (isAbilityUsed ? remainingAbilityCount-1 : remainingAbilityCount));

        selectedPlayerText.setText(isAbilityUsed ? "Choosen player is: " + currentPlayer.getRole().getChoosenPlayer().getNameAndNumber()
                : "You are not using your ability!");

        if(time !=Time.NIGHT){
            nextText.setVisibility(GONE);
            selectedPlayerText.setVisibility(GONE);
        }
    }

}


