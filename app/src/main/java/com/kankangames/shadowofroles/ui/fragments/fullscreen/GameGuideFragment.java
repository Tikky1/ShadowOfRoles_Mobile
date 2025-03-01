package com.kankangames.shadowofroles.ui.fragments.fullscreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kankangames.shadowofroles.R;
import com.kankangames.shadowofroles.ui.adapters.GameGuideAdapter;

public class GameGuideFragment extends FullScreenFragment{

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private GameGuideAdapter gameGuideAdapter;
    public GameGuideFragment() {
        super(null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_game_guide, null);
        builder.setView(view);


        AlertDialog dialog = builder.create();
        tabLayout = view.findViewById(R.id.tab_layout_game_guide);
        viewPager = view.findViewById(R.id.view_pager_game_guide);

        gameGuideAdapter = new GameGuideAdapter(this);
        viewPager.setAdapter(gameGuideAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Game Rules");
                    break;
                case 1:
                    tab.setText("Role Guide");
                    break;
                case 2:
                    tab.setText("Main Goal");
            }
        }).attach();

        Button closeButton = view.findViewById(R.id.close_game_guide_button);
        closeButton.setOnClickListener(v -> dismiss());

        return dialog;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_game_guide;
    }
}
