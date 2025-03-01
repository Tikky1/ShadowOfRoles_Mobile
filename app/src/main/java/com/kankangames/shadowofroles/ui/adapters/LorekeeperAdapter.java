package com.kankangames.shadowofroles.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.models.roles.templates.RoleTemplate;

import java.util.Comparator;
import java.util.List;

public class LorekeeperAdapter extends ArrayAdapter<RoleTemplate>{

    private final List<RoleTemplate> roles;
    private final Context context;

    public LorekeeperAdapter(@NonNull Context context, List<RoleTemplate> roles) {
        super(context, R.layout.lore_keeper_spinner_item);
        this.context = context;
        this.roles = roles;
        roles.sort(Comparator.comparing(RoleTemplate::getWinningTeam));

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


        RoleTemplate currentRole = roles.get(position);


        TextView textView = convertView.findViewById(R.id.lore_keeper_item_role_name);
        textView.setText(currentRole.getName());


        Drawable background;
        switch (currentRole.getWinningTeam().getTeam()) {
            case FOLK:
                background = ContextCompat.getDrawable(context, R.drawable.role_box_folk);
                break;
            case CORRUPTER:
                background = ContextCompat.getDrawable(context, R.drawable.role_box_corrupt);
                break;
            default:
                background = ContextCompat.getDrawable(context, R.drawable.role_box_neutral);
                break;
        }


        convertView.setBackground(background);

        return convertView;
    }

}
