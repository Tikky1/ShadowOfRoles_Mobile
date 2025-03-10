package com.kankangames.shadowofroles.ui.adapters;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.gamestate.Time;
import com.kankangames.shadowofroles.models.player.Player;
import com.kankangames.shadowofroles.models.roles.enums.WinningTeam;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.models.roles.templates.corrupterroles.support.LastJoke;
import com.kankangames.shadowofroles.models.roles.enums.Team;
import com.kankangames.shadowofroles.models.roles.templates.folkroles.protector.FolkHero;
import com.kankangames.shadowofroles.models.roles.templates.neutralroles.good.Lorekeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlayersViewAdapter extends RecyclerView.Adapter<PlayersViewAdapter.ViewHolder>{

    private final List<ViewHolder> allSelectionBoxes = new ArrayList<>();
    private final Context context;
    private ArrayList<Player> players = new ArrayList<>();

    private final Time time;

    private final Player currentPlayer;

    private final int itemMargin;


    public PlayersViewAdapter(Time time, Player currentPlayer, Context context) {
        this.time = time;
        this.currentPlayer = currentPlayer;
        this.context = context;
        this.itemMargin = context.getResources().getDimensionPixelSize(R.dimen.player_box_margin);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_player_selection, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();

        layoutParams.setMargins(itemMargin, itemMargin, itemMargin, itemMargin);
        view.setLayoutParams(layoutParams);

        allSelectionBoxes.add(viewHolder);
        viewHolder.setAllSelectionBoxes(allSelectionBoxes);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.setCurrentPlayerAndOtherPlayer(currentPlayer,player);
        holder.playerNumberView.setText(String.format(Locale.ROOT, "%d", player.getNumber()));
        holder.playerNameView.setText(player.getName());


        Button selectButton = holder.selectionBtn;

        boolean isPlayerCurrentPlayer = isPlayerCurrentPlayer(player, currentPlayer);
        if (time == Time.DAY) {
            selectButton.setVisibility(GONE);
        }
        else if(time == Time.NIGHT){


            switch (currentPlayer.getRole().getTemplate().getAbilityType()) {

                case ACTIVE_SELF:
                    selectButton.setVisibility(isPlayerCurrentPlayer ? VISIBLE : GONE);
                    break;

                case ACTIVE_ALL:
                    selectButton.setVisibility(VISIBLE);
                    break;

                case ACTIVE_OTHERS:
                    selectButton.setVisibility(isPlayerCurrentPlayer ? View.GONE : View.VISIBLE);
                    break;

                case OTHER_THAN_CORRUPTER:
                    selectButton.setVisibility(
                            player.getRole().getTemplate().getWinningTeam().getTeam() != Team.CORRUPTER ? View.VISIBLE : View.GONE);
                    break;

                default:
                    selectButton.setVisibility(GONE);
            }

            if (currentPlayer.getRole().getTemplate() instanceof Lorekeeper) {
                Lorekeeper lorekeeper = (Lorekeeper) currentPlayer.getRole().getTemplate();
                if (lorekeeper.getAlreadyChosenPlayers().contains(player)) {
                    selectButton.setVisibility(GONE);
                }
            }

            if (currentPlayer.getRole().getTemplate() instanceof LastJoke && currentPlayer.getDeathProperties().isAlive()) {
                selectButton.setVisibility(GONE);
            }

            if(currentPlayer.getRole().getTemplate() instanceof FolkHero){
                FolkHero folkHero = (FolkHero) currentPlayer.getRole().getTemplate();
                if(folkHero.getRemainingAbilityCount()<=0){
                    selectButton.setVisibility(GONE);
                }
            }
        }
        else{
            selectButton.setVisibility(isPlayerCurrentPlayer ? View.GONE : View.VISIBLE);
        }

        holder.selectionBtn.setText(holder.isSelected
                ? context.getString(R.string.unselect)
                : context.getString(R.string.select));



        RoleTemplate roleTemplate = player.getRole().getTemplate();
        holder.roleName.setText(String.format("(%s)", roleTemplate.getName()));


        int color;
        switch (roleTemplate.getWinningTeam().getTeam()){
            case FOLK:
                color = ContextCompat.getColor(selectButton.getContext(),R.color.folk_color);
                break;
            case CORRUPTER:
                color = ContextCompat.getColor(selectButton.getContext(),R.color.corruptor_color);
                break;
            default:
                color = ContextCompat.getColor(selectButton.getContext(),R.color.neutral_color);
                break;
        }

        holder.roleName.setTextColor(color);

        boolean isRevealed = player.getRole().isRevealed();
        boolean areBothCorrupter = roleTemplate.getWinningTeam() == WinningTeam.CORRUPTER
                && currentPlayer.getRole().getTemplate().getWinningTeam() == WinningTeam.CORRUPTER;

        if(isRevealed||areBothCorrupter||isPlayerCurrentPlayer){
            holder.roleName.setVisibility(VISIBLE);
        } else {
            holder.roleName.setVisibility(GONE);
        }


    }

    private boolean isPlayerCurrentPlayer(Player player, Player currentPlayer){
        return player.getNumber() == currentPlayer.getNumber();
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView playerNameView;
        private final TextView playerNumberView;
        private final Button selectionBtn;
        private final TextView roleName;
        private final FrameLayout numberCircle;

        private boolean isSelected;

        private Player currentPlayer;

        private Player player;

        private List<ViewHolder> allSelectionBoxes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameView = itemView.findViewById(R.id.player_name);
            playerNumberView = itemView.findViewById(R.id.player_number);
            selectionBtn = itemView.findViewById(R.id.player_button);
            numberCircle = itemView.findViewById(R.id.number_circle);
            roleName = itemView.findViewById(R.id.role_name);

            isSelected = false;
            setButtonEvent(itemView);
        }


        private void setButtonEvent(View itemView){
            selectionBtn.setOnClickListener(v -> {

                isSelected = !isSelected;
                currentPlayer.getRole().setChoosenPlayer(isSelected ? player : null);

                boolean tempSelected = isSelected;
                for (ViewHolder viewHolder : allSelectionBoxes) {

                    viewHolder.isSelected = false;
                    viewHolder.selectionBtn.setText(R.string.select);

                }
                isSelected = tempSelected;

                selectionBtn.setText(isSelected
                        ? itemView.getContext().getString(R.string.unselect)
                        : itemView.getContext().getString(R.string.select));

            });
        }

        private void setCurrentPlayerAndOtherPlayer(Player currentPlayer, Player player) {
            this.currentPlayer = currentPlayer;
            this.player = player;
            if(currentPlayer.getNumber() == player.getNumber()){
                numberCircle.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.circle_background_current));
            }
        }

        public void setAllSelectionBoxes(List<ViewHolder> allSelectionBoxes) {
            this.allSelectionBoxes = allSelectionBoxes;
        }
    }
}
