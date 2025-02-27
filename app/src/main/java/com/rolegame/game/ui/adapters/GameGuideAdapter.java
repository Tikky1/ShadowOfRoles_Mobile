package com.rolegame.game.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rolegame.game.ui.fragments.AllRolesFragment;
import com.rolegame.game.ui.fragments.MainGoalFragment;
import com.rolegame.game.ui.fragments.RulesFragment;

public class GameGuideAdapter extends FragmentStateAdapter {

    public GameGuideAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new RulesFragment();
            case 1:
                return new AllRolesFragment();
            default:
                return new MainGoalFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
