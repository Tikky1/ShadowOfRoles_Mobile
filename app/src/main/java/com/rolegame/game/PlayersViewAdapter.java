package com.rolegame.game;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rolegame.game.gamestate.Time;
import com.rolegame.game.models.player.Player;
import com.rolegame.game.models.roles.corrupterroles.support.LastJoke;
import com.rolegame.game.models.roles.enums.Team;
import com.rolegame.game.models.roles.neutralroles.good.Lorekeeper;

import java.util.ArrayList;
import java.util.List;

public class PlayersViewAdapter extends RecyclerView.Adapter<PlayersViewAdapter.ViewHolder>{

    private static final List<ViewHolder> allSelectionBoxes = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

    private final Time time;

    private final Player currentPlayer;


    public PlayersViewAdapter(Time time, Player currentPlayer) {
        this.time = time;
        this.currentPlayer = currentPlayer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_selection_box, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        allSelectionBoxes.add(viewHolder);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.setCurrentPlayer(currentPlayer);
        holder.setPlayer(player);
        holder.playerNumberView.setText(player.getNumber()+"");
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
                            player.getRole().getTemplate().getTeam() != Team.CORRUPTER ? View.VISIBLE : View.GONE);
                    break;

                default:
                    selectButton.setVisibility(GONE);
            }

            if (currentPlayer.getRole().getTemplate() instanceof Lorekeeper) {
                Lorekeeper lorekeeper = (Lorekeeper) currentPlayer.getRole().getTemplate();
                if (lorekeeper.getAlreadyChosenPlayers().contains(player)) {
                    selectButton.setVisibility(View.GONE);
                }
            }

            if (currentPlayer.getRole().getTemplate() instanceof LastJoke && currentPlayer.isAlive()) {
                selectButton.setVisibility(GONE);
            }
        }
        else{
            selectButton.setVisibility(isPlayerCurrentPlayer ? View.GONE : View.VISIBLE);
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
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView playerNameView;
        private final TextView playerNumberView;
        private final Button selectionBtn;

        private boolean isSelected = false;

        private Player currentPlayer;

        private Player player;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerNameView = itemView.findViewById(R.id.player_name);
            playerNumberView = itemView.findViewById(R.id.player_number);
            selectionBtn = itemView.findViewById(R.id.player_button);
            setButtonEvent();
        }


        private void setButtonEvent(){
            selectionBtn.setOnClickListener(v -> {

                isSelected = !isSelected;
                currentPlayer.getRole().setChoosenPlayer(isSelected ? player : null);
                for (ViewHolder viewHolder : allSelectionBoxes) {
                    if(viewHolder!=this){
                        viewHolder.isSelected = false;
                        viewHolder.selectionBtn.setText("Select");
                    }

                }

                selectionBtn.setText(isSelected ? "Unselect" : "Select");
            });
        }

        public void setCurrentPlayer(Player currentPlayer) {
            this.currentPlayer = currentPlayer;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }
    }
}
