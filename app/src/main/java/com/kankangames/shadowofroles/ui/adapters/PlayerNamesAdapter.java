package com.kankangames.shadowofroles.ui.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;

import java.util.List;

public class PlayerNamesAdapter extends RecyclerView.Adapter<PlayerNamesAdapter.ViewHolderPlayerNames> {

    private final List<String> playerNames;
    private final List<Boolean> isPlayersAI;

    public void updateList(){

    }
    public PlayerNamesAdapter(List<String> playerNames, List<Boolean> isPlayersAI) {
        this.playerNames = playerNames;
        this.isPlayersAI = isPlayersAI;
    }

    @NonNull
    @Override
    public ViewHolderPlayerNames onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_player_names, parent, false);
        return new ViewHolderPlayerNames(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPlayerNames holder, int position) {
        holder.playerName.setText(playerNames.get(position));
        holder.isAICheckBox.setChecked(isPlayersAI.get(position));

        // Listen for changes in the input fields
        holder.playerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Update the player name in the list when the text changes
                playerNames.set(holder.getAdapterPosition(), charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });



        holder.isAICheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(buttonView.isPressed()){

                isPlayersAI.set(position, isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return playerNames.size();
    }

    public static class ViewHolderPlayerNames extends RecyclerView.ViewHolder {

        private final EditText playerName;
        private final CheckBox isAICheckBox;

        public ViewHolderPlayerNames(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.online_player_name);
            isAICheckBox = itemView.findViewById(R.id.is_player_ai_checkbox);

            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;
                marginParams.setMargins(0, 0, 0, 16);
                itemView.setLayoutParams(marginParams);
            }
        }

    }
}
