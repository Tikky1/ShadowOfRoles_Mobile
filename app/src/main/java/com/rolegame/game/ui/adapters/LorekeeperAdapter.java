package com.rolegame.game.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rolegame.game.R;
import com.rolegame.game.models.roles.templates.RoleTemplate;
import com.rolegame.game.models.roles.templates.neutralroles.good.Lorekeeper;

import java.util.List;

public class LorekeeperAdapter extends ArrayAdapter<RoleTemplate>{

    private final List<RoleTemplate> roles;
    //private final Lorekeeper lorekeeper;
    private final Context context;

    public LorekeeperAdapter(@NonNull Context context, List<RoleTemplate> roles) {
        super(context, R.layout.lore_keeper_spinner_item);
        this.context = context;
        this.roles = roles;
        //this.lorekeeper = lorekeeper;

    }

    @Override
    public int getCount() {
        return roles.size();
    }

    @Nullable
    @Override
    public RoleTemplate getItem(int position) {
        return roles.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.lore_keeper_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.lore_keeper_item_role_name);
        textView.setText(roles.get(position).getName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.lore_keeper_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.lore_keeper_item_role_name);
        textView.setText(roles.get(position).getName());
        return convertView;
    }
}
