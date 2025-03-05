package com.kankangames.shadowofroles.ui.fragments;

import static android.view.View.GONE;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.protector.FolkHero;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.unique.Entrepreneur;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.good.Lorekeeper;
import com.kankangames.shadowofroles.services.GameService;
import com.kankangames.shadowofroles.services.RoleService;
import com.kankangames.shadowofroles.services.StartGameService;
import com.kankangames.shadowofroles.ui.adapters.LorekeeperAdapter;

import java.util.Locale;


public class PlayerRoleFragment extends Fragment {

    private Player currentPlayer;
    private GameService gameService;
    private Time time;
    private TextManager textManager;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_role_page, container, false);

        gameService = StartGameService.getInstance().getGameService();
        time = gameService.getTimeService().getTime();
        currentPlayer = gameService.getCurrentPlayer();
        textManager = TextManager.getInstance();

        setRoleInfoLayout(view);
        setChosenPlayerText(view);


        switch (currentPlayer.getRole().getTemplate().getId()){
            case Lorekeeper:
                setLoreKeeperInfo(view, inflater);
                break;

            case Entrepreneur:
                setEntrepreneurInfo(view,inflater);
                break;

            case FolkHero:
                setFolkHeroInfo(view, inflater);
                break;

        }

        return view;
    }



    private void setRoleInfoLayout(View view) {
        RoleTemplate currentRole = currentPlayer.getRole().getTemplate();

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

    private void setChosenPlayerText(View view){
        Player chosenPlayer = currentPlayer.getRole().getChoosenPlayer();
        TextView chosenPlayerText = view.findViewById(R.id.chosen_player_text);
        String abilityText;

        if(time == Time.VOTING){
            if(chosenPlayer==null){
                abilityText = getString(R.string.chosen_player_text_voting_nobody);
            }else{
                abilityText = getString(R.string.chosen_player_text_voting);
            }
        }

        else if(time == Time.NIGHT){
            switch (currentPlayer.getRole().getTemplate().getAbilityType()){
                case PASSIVE:
                case NO_ABILITY:
                    abilityText = getString(R.string.chosen_player_text_night_no_ability);
                    break;

                default:
                    if(chosenPlayer==null){
                        abilityText = getString(R.string.chosen_player_text_night_nobody);
                    }
                    else{
                        abilityText = getString(R.string.chosen_player_text_night);
                    }
                    break;
            }
        }
        else{
            abilityText = "";
            chosenPlayerText.setVisibility(GONE);
        }

        if(chosenPlayer!=null){
            abilityText = abilityText.replace("{playerName}", chosenPlayer.getNameAndNumber());
        }

        chosenPlayerText.setText(abilityText);

    }

    private void setLoreKeeperInfo(View view, LayoutInflater inflater){
        Lorekeeper lorekeeper = (Lorekeeper) currentPlayer.getRole().getTemplate();
        FrameLayout lorekeeperLayout = view.findViewById(R.id.unique_roles_layout);
        ViewGroup spinnerBox = (ViewGroup) inflater.inflate(R.layout.lore_keeper_box, lorekeeperLayout, true);
        Spinner spinner = spinnerBox.findViewById(R.id.lorekeeper_spinner);
        Button selectButton = spinnerBox.findViewById(R.id.role_select_button);
        Button noneButton = spinnerBox.findViewById(R.id.role_select_none_button);
        TextView chosenRoleText = spinnerBox.findViewById(R.id.selected_role_text);
        TextView currentGuessCountText = spinnerBox.findViewById(R.id.remaining_winning_guess_count);
        TextView winningGuessCountText = spinnerBox.findViewById(R.id.winning_guess_count);

        LorekeeperAdapter lorekeeperAdapter = new LorekeeperAdapter(view.getContext(), RoleService.getAllRoles());
        spinner.setAdapter(lorekeeperAdapter);
        selectButton.setOnClickListener(v -> {
            lorekeeper.setGuessedRole((RoleTemplate) spinner.getSelectedItem());
            setLoreKeeperSelectedRole(lorekeeper, chosenRoleText);
        });
        noneButton.setOnClickListener(v -> {
            lorekeeper.setGuessedRole(null);
            setLoreKeeperSelectedRole(lorekeeper, chosenRoleText);
        });
        setLoreKeeperSelectedRole(lorekeeper,chosenRoleText);

        int currentGuessCount = lorekeeper.getTrueGuessCount();
        int winningGuessCount = gameService.getSpecialRolesService().LORE_KEEPER_WINNING_COUNT;
        currentGuessCountText.setText(String.format(Locale.ROOT, getString(R.string.lorekeeper_current_true_guess_count),
                currentGuessCount));
        winningGuessCountText.setText(String.format(Locale.ROOT, getString(R.string.lorekeeper_winning_true_guess_count),
                winningGuessCount));
    }
    private void setLoreKeeperSelectedRole(Lorekeeper lorekeeper, TextView textView){
        textView.setText((lorekeeper.getGuessedRole()==null)
                ? getString(R.string.lorekeeper_guessed_role_none)
                : String.format(Locale.ROOT, getString(R.string.lorekeeper_guessed_role),lorekeeper.getGuessedRole().getName()));
    }

    private void setEntrepreneurInfo(View view, LayoutInflater inflater){
        Entrepreneur entrepreneur = (Entrepreneur) currentPlayer.getRole().getTemplate();
        FrameLayout entrepreneurLayout = view.findViewById(R.id.unique_roles_layout);
        ViewGroup entrepreneurBox = (ViewGroup) inflater.inflate(R.layout.entrepreneur_box, entrepreneurLayout,true);
        TextView currentMoneyText = entrepreneurBox.findViewById(R.id.entrepreneur_currentmoney_text);

        TextView expectedMoneyText = entrepreneurBox.findViewById(R.id.entrepreneur_expected_money_text);
        RelativeLayout infoBtn = entrepreneurBox.findViewById(R.id.entrepreneur_info_btn);
        RelativeLayout healBtn = entrepreneurBox.findViewById(R.id.entrepreneur_heal_btn);
        RelativeLayout attackBtn = entrepreneurBox.findViewById(R.id.entrepreneur_attack_btn);
        RelativeLayout passBtn = entrepreneurBox.findViewById(R.id.entrepreneur_pass_btn);

        TextView infoCostText = entrepreneurBox.findViewById(R.id.info_cost);
        TextView healCostText = entrepreneurBox.findViewById(R.id.heal_cost);
        TextView attackCostText = entrepreneurBox.findViewById(R.id.attack_cost);

        entrepreneurExpectedMoney(entrepreneur, expectedMoneyText);

        int currentMoney = entrepreneur.getMoney();
        currentMoneyText.setText(String.format(Locale.ROOT, getString(R.string.entrepreneur_current_money), currentMoney));

        infoCostText.setText(String.format(Locale.ROOT, getString(R.string.entrepreneur_cost), Entrepreneur.ChosenAbility.INFO.getPrice()));
        healCostText.setText(String.format(Locale.ROOT, getString(R.string.entrepreneur_cost), Entrepreneur.ChosenAbility.HEAL.getPrice()));
        attackCostText.setText(String.format(Locale.ROOT, getString(R.string.entrepreneur_cost), Entrepreneur.ChosenAbility.ATTACK.getPrice()));

        infoBtn.setOnClickListener(v -> {
            entrepreneur.setChosenAbility(Entrepreneur.ChosenAbility.INFO);
            entrepreneurExpectedMoney(entrepreneur, expectedMoneyText);
        });

        healBtn.setOnClickListener(v -> {
            entrepreneur.setChosenAbility(Entrepreneur.ChosenAbility.HEAL);
            entrepreneurExpectedMoney(entrepreneur, expectedMoneyText);
        });

        attackBtn.setOnClickListener(v -> {
            entrepreneur.setChosenAbility(Entrepreneur.ChosenAbility.ATTACK);
            entrepreneurExpectedMoney(entrepreneur, expectedMoneyText);
        });

        passBtn.setOnClickListener(v -> {
            entrepreneur.setChosenAbility(Entrepreneur.ChosenAbility.NONE);
            entrepreneurExpectedMoney(entrepreneur, expectedMoneyText);
        });


    }

    private void entrepreneurExpectedMoney(Entrepreneur entrepreneur, TextView textView){
        int currentMoney = entrepreneur.getMoney();
        int abilityMoney = entrepreneur.getChosenAbility().getPrice();

        Context context = textView.getContext();

        String message;
        if (currentMoney >= abilityMoney) {
            message = String.format(
                    context.getString(R.string.entrepreneur_selected) + " "
                            + context.getString(R.string.entrepreneur_expected_money)
                    , textManager.getTextPrefix(entrepreneur.getChosenAbility().name(),"entrepreneur")
                    , (currentMoney - abilityMoney)
            );
        } else {
            message = String.format(
                    context.getString(R.string.entrepreneur_selected) +
                            context.getString(R.string.money_insufficient),
                    textManager.getTextPrefix(entrepreneur.getChosenAbility().name(),"entrepreneur")
            );
        }
        textView.setText(message);

    }

    private void setFolkHeroInfo(View view, LayoutInflater inflater){
        FolkHero folkHero = (FolkHero) currentPlayer.getRole().getTemplate();

        FrameLayout folkHeroLayout = view.findViewById(R.id.unique_roles_layout);
        ViewGroup folkHeroBox = (ViewGroup) inflater.inflate(R.layout.folk_hero_box, folkHeroLayout, true);
        TextView currentText = folkHeroBox.findViewById(R.id.folk_hero_this_night_text_view);
        TextView nextText = folkHeroBox.findViewById(R.id.folk_hero_next_night_text);

        Context context = currentText.getContext();

        int remainingAbilityCount = folkHero.getRemainingAbilityCount();
        currentText.setText(String.format(context.getString(R.string.folkhero_remaining_ability_count), remainingAbilityCount));

        boolean isAbilityUsed = currentPlayer.getRole().getChoosenPlayer() != null;
        int expectedCount = isAbilityUsed ? remainingAbilityCount - 1 : remainingAbilityCount;

        nextText.setText(String.format(context.getString(R.string.folkhero_expected_ability_count), expectedCount));

        if(time !=Time.NIGHT){
            nextText.setVisibility(GONE);
        }
    }

}


