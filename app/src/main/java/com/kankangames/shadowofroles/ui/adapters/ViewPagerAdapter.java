package com.kankangames.shadowofroles.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kankangames.shadowofroles.game.models.gamestate.GameMode;
import com.kankangames.shadowofroles.ui.fragments.AllRolesFragment;
import com.kankangames.shadowofroles.ui.fragments.PlayerRoleFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final GameMode gameMode;
    public ViewPagerAdapter(@NonNull Fragment fragment, GameMode gameMode){
        super(fragment);
        this.gameMode = gameMode;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PlayerRoleFragment(gameMode);
            case 1:
                return new AllRolesFragment();
            default:
                return new AllRolesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
