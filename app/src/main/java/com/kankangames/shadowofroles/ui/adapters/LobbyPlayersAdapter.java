package com.kankangames.shadowofroles.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.managers.TextManager;
import com.kankangames.shadowofroles.models.player.LobbyPlayer;

import java.util.List;

public class LobbyPlayersAdapter extends RecyclerView.Adapter<LobbyPlayersAdapter.PlayerLobbyViewHolder>{

    private final List<LobbyPlayer> players;
    private final Context context;

    private int selectedPosition = RecyclerView.NO_POSITION;

    @NonNull
    @Override
    public PlayerLobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_player_lobby, parent, false);
        return new PlayerLobbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerLobbyViewHolder holder, int position) {
        LobbyPlayer player = players.get(position);

        holder.playerName.setText(player.getName());
        holder.playerStatus.setText(TextManager.getInstance().getTextEnum(player.getLobbyPlayerStatus().name()));
        holder.playerImage.setImageDrawable(player.isAI() ?
                ContextCompat.getDrawable(context,R.drawable.bot) :
                ContextCompat.getDrawable(context,R.drawable.logopng)
        );

        if (selectedPosition == holder.getAdapterPosition()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedItem));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        }

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
        });
    }



    @Override
    public int getItemCount() {
        return players.size();
    }

    public LobbyPlayersAdapter(List<LobbyPlayer> players, Context context) {
        this.players = players;
        this.context = context;
    }

    public static class PlayerLobbyViewHolder extends RecyclerView.ViewHolder{
        private final TextView playerName;
        private final TextView playerStatus;
        private final ImageView playerImage;
        public PlayerLobbyViewHolder(@NonNull View itemView) {
            super(itemView);

            playerName = itemView.findViewById(R.id.playerName);
            playerStatus = itemView.findViewById(R.id.playerStatus);
            playerImage = itemView.findViewById(R.id.playerAvatar);
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
