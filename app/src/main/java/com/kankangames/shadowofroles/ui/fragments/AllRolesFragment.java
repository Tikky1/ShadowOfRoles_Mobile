package com.kankangames.shadowofroles.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.game.models.roles.templates.RoleTemplate;
import com.kankangames.shadowofroles.game.services.RoleService;
import com.kankangames.shadowofroles.ui.adapters.RolesViewAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AllRolesFragment extends Fragment {

    private TextView teamText;
    private TextView abilityText;
    private TextView attributesText;
    private TextView goalText;
    private TextView roleText;

    public AllRolesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_roles, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.roles_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        List<RoleTemplate> roleTemplates = RoleService.getAllRoles();
        roleTemplates.sort(Comparator.comparing(RoleTemplate::getWinningTeam));

        RolesViewAdapter rolesViewAdapter = new RolesViewAdapter(
                (ArrayList<RoleTemplate>) roleTemplates, this::setTextViews);
        recyclerView.setAdapter(rolesViewAdapter);


        teamText = view.findViewById(R.id.all_team_text);
        abilityText = view.findViewById(R.id.all_ability_text);
        attributesText = view.findViewById(R.id.all_attributes_text);
        goalText = view.findViewById(R.id.all_goal_text);
        roleText = view.findViewById(R.id.all_role_text);

        setTextViews(RoleService.getAllRoles().get(0));

        return view;
    }

    private void setTextViews(RoleTemplate currentRole){
        roleText.setText(currentRole.getName());
        teamText.setText(currentRole.getTeamText());
        abilityText.setText(currentRole.getAbilities());
        attributesText.setText(currentRole.getAttributes());
        goalText.setText(currentRole.getGoal());
    }
}
