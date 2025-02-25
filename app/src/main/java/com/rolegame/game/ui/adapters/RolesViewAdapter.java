package com.rolegame.game.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rolegame.game.R;
import com.rolegame.game.models.roles.templates.RoleTemplate;

import java.util.ArrayList;

public class RolesViewAdapter extends RecyclerView.Adapter<RolesViewAdapter.RolesViewHolder> {

    private final Clickable clickable;

    private final ArrayList<RoleTemplate> roles;

    public RolesViewAdapter(ArrayList<RoleTemplate> roles, Clickable clickable) {
        this.roles = roles;
        this.clickable = clickable;
    }

    @NonNull
    @Override
    public RolesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_role_name, parent, false);
        RolesViewHolder rolesViewHolder = new RolesViewHolder(view);


        return rolesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RolesViewHolder holder, int position) {
        holder.roleName.setText(roles.get(position).getName());

        RelativeLayout root = holder.layout;
        root.setOnClickListener(v -> {
            clickable.roleClicked(roles.get(position));
        });

        Context context = holder.layout.getContext();

        RoleTemplate currentRole = roles.get(position);
        Drawable background;
        switch (currentRole.getWinningTeam().getTeam()){
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
        holder.layout.setBackground(background);
    }

    @Override
    public int getItemCount() {
        return roles.size();
    }

    public interface Clickable{
        void roleClicked(RoleTemplate currentRole);
    }

    public static class RolesViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout layout;
        private final TextView roleName;
        public RolesViewHolder(@NonNull View itemView) {
            super(itemView);
            roleName = itemView.findViewById(R.id.role_name_text_view);
            layout = itemView.findViewById(R.id.role_name_layout);
        }
    }
}
