package com.rolegame.game.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rolegame.game.R;
import com.rolegame.game.models.player.Player;

import java.util.List;

public class GraveyardViewAdapter extends RecyclerView.Adapter<GraveyardViewAdapter.GraveyardViewHolder> {

    private List<Player> deadPlayers;

    @NonNull
    @Override
    public GraveyardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.graveyard_box, parent, false);
        GraveyardViewHolder viewHolder = new GraveyardViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GraveyardViewHolder holder, int position) {
        holder.setTexts(deadPlayers.get(position));
    }

    @Override
    public int getItemCount() {
        return deadPlayers.size();
    }

    public void setDeadPlayers(List<Player> deadPlayers) {
        this.deadPlayers = deadPlayers;
        notifyDataSetChanged();
    }

    public static class GraveyardViewHolder extends RecyclerView.ViewHolder {

        private final TextView causeOfDeathText;
        private final TextView nameText;

        public GraveyardViewHolder(@NonNull View itemView) {
            super(itemView);
            causeOfDeathText = itemView.findViewById(R.id.graveyard_cause_of_death);
            nameText = itemView.findViewById(R.id.graveyard_player_name);

            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
                marginParams.setMargins(0, 0, 0, 16);
                itemView.setLayoutParams(marginParams);
            }

        }

        public void setTexts(Player player) {

            causeOfDeathText.setText("Cause of Death(s): " +player.getCausesOfDeathAsString());
            nameText.setText(player.getNameAndRole());

        }
    }
}
