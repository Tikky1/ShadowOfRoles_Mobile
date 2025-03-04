package com.kankangames.shadowofroles.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.models.player.Player;

import java.util.List;

public class GraveyardViewAdapter extends RecyclerView.Adapter<GraveyardViewAdapter.GraveyardViewHolder> {

    private final List<Player> deadPlayers;
    private final Context context;

    @NonNull
    @Override
    public GraveyardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_graveyard, parent, false);
        GraveyardViewHolder viewHolder = new GraveyardViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GraveyardViewHolder holder, int position) {
        holder.setTexts(deadPlayers.get(position), context);
    }

    @Override
    public int getItemCount() {
        return deadPlayers.size();
    }

    public GraveyardViewAdapter(List<Player> deadPlayers, Context context) {
        this.deadPlayers = deadPlayers;
        this.context = context;
    }

    public static class GraveyardViewHolder extends RecyclerView.ViewHolder {

        private final TextView causeOfDeathText;
        private final TextView nameText;
        private final TextView timeText;

        public GraveyardViewHolder(@NonNull View itemView) {
            super(itemView);
            causeOfDeathText = itemView.findViewById(R.id.graveyard_cause_of_death);
            nameText = itemView.findViewById(R.id.graveyard_player_name);
            timeText = itemView.findViewById(R.id.graveyard_time_of_death);

            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
                marginParams.setMargins(0, 0, 0, 16);
                itemView.setLayoutParams(marginParams);
            }

        }

        public void setTexts(Player player, Context context) {

            causeOfDeathText.setText(
                    String.format(context.getString(R.string.causes_of_death),
                            player.getDeathProperties().getCausesOfDeathAsString())
            );

            nameText.setText(player.getNameAndRole());

            timeText.setText(
                    String.format(context.getString(R.string.graveyard_death_time),
                            player.getDeathProperties().getDeathTimeAndDayCount())
            );

        }
    }
}
