package com.kankangames.shadowofroles.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;

import java.util.List;

public class ServersAdapter extends RecyclerView.Adapter<ServersAdapter.GameViewHolder> {

    private final List<String> gameList;
    private final OnJoinClickListener onJoinClickListener;

    public interface OnJoinClickListener {
        void onJoinClick(String lobby);
    }

    public ServersAdapter(List<String> gameList, OnJoinClickListener onJoinClickListener) {
        this.gameList = gameList;
        this.onJoinClickListener = onJoinClickListener;
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView gameName;
        Button joinButton;

        public GameViewHolder(View itemView) {
            super(itemView);
            gameName = itemView.findViewById(R.id.tv_game_name);
            joinButton = itemView.findViewById(R.id.btn_join_game);
        }
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_servers, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        String game = gameList.get(position);
        holder.gameName.setText(game);
        holder.joinButton.setOnClickListener(v -> onJoinClickListener.onJoinClick(game));
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}
