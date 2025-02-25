package com.rolegame.game.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rolegame.game.models.roles.Role;
import com.rolegame.game.ui.fragments.PlayerRoleFragment;
import com.rolegame.game.ui.fragments.RoleGuideFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull Fragment fragment){
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PlayerRoleFragment();
            case 1:
                return new RoleGuideFragment();
            default:
                return new PlayerRoleFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
